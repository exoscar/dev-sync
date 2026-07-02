package org.devsync.spring.issue.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.issue.context.IssueContext;
import org.devsync.spring.issue.dto.*;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.entity.IssuePriority;
import org.devsync.spring.issue.entity.IssueStatus;
import org.devsync.spring.issue.event.IssueAssignedEvent;
import org.devsync.spring.issue.event.IssuePriorityChangedEvent;
import org.devsync.spring.issue.event.IssueStatusChangedEvent;
import org.devsync.spring.issue.factory.IssueFactory;
import org.devsync.spring.issue.mapper.IssueMapper;
import org.devsync.spring.issue.repository.IssueRepository;
import org.devsync.spring.issue.specification.IssueSpecification;
import org.devsync.spring.project.entity.Project;
import org.devsync.spring.watcher.service.IssueWatcherService;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.repository.WorkspaceMemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final CurrentUserService currentUserService;
    private final IssueRepository issueRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final IssueMapper mapper;
    private final IssueAuthorizationService issueAuthorizationService;
    private final IssueValidationService validationService;
    private final IssueAccessService issueAccessService;
    private final IssueActivityUtilService activityService;
    private final IssueFactory issueFactory;
    private final IssueWatcherService issueWatcherService;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public IssueResponse createIssue(String projectId, @Valid CreateIssueRequest request) {
        IssueContext context = issueAccessService.loadProjectContext(projectId);
        Project project = context.project();
        WorkspaceMember member = context.member();
        issueAuthorizationService.requireContributor(member);
        Issue issue = issueFactory.create(project, request);
        issueRepository.save(issue);
        issueWatcherService.addCreatorWatcher(issue,context.member().getUser());
        activityService.issueCreated(issue, member.getUser());
        return mapper.toResponse(issue);
    }

    public Page<IssueResponse> getAllIssues(String projectId, int page, int size, IssueFilterRequest filterRequest) {
        Project project = issueAccessService.loadProjectContext(projectId).project();
        Specification<Issue> spec = IssueSpecification.hasProject(project.getId());
        if (filterRequest.getStatus() != null) {
            spec = spec.and(IssueSpecification.hasStatus(filterRequest.getStatus()));
        }
        if (filterRequest.getPriority() != null) {
            spec = spec.and(IssueSpecification.hasPriority(filterRequest.getPriority()));
        }
        if (filterRequest.getAssigneeId() != null) {
            spec = spec.and(IssueSpecification.hasAssignee(filterRequest.getAssigneeId()));
        }
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Issue> issues = issueRepository.findAll(spec, pageable);
        return issues.map(mapper::toResponse);
    }

    public Page<IssueResponse> getMyAssignedIssues(String projectId, IssueStatus status, IssuePriority priority, int page, int size) {
        IssueFilterRequest filterRequest = IssueFilterRequest.builder().status(status).priority(priority).assigneeId(currentUserService.getCurrentUserId()).build();
        return getAllIssues(projectId, page, size, filterRequest);
    }

    @Transactional
    public IssueResponse updateIssue(String projectId, String issueId, UpdateIssueRequest request) {
        IssueContext context = issueAccessService.loadIssueContext(projectId, issueId);
        WorkspaceMember member = context.member();
        issueAuthorizationService.requireContributor(member);
        Issue issue = context.issue();
        issue.setTitle(request.getTitle().trim());
        issue.setDescription(request.getDescription().trim());
        activityService.issueUpdated(issue, member.getUser());
        return mapper.toResponse(issue);
    }

    public IssueResponse getIssueById(String projectId, String issueId) {
        IssueContext context = issueAccessService.loadIssueContext(projectId, issueId);
        Issue issue = context.issue();
        return mapper.toResponse(issue);
    }

    @Transactional
    public IssueResponse updateStatus(String projectId, String issueId, @Valid UpdateStatusRequest request) {
        IssueContext context = issueAccessService.loadIssueContext(projectId, issueId);
        WorkspaceMember member = context.member();
        issueAuthorizationService.requireContributor(member);
        Issue issue = context.issue();
        validationService.validateStatusChange(issue, request.getStatus());
        IssueStatus oldStatus = issue.getStatus();
        issue.setStatus(request.getStatus());
        activityService.issueStatusChanged(issue, member.getUser(), oldStatus);
        eventPublisher.publishEvent(new IssueStatusChangedEvent(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                member.getUser().getId(),
                oldStatus,
                request.getStatus(),
                issue.getProject().getWorkspace().getId(),
                issue.getProject().getWorkspace().getName(),
                issue.getProject().getId(),
                issue.getProject().getName()
        ));
        return mapper.toResponse(issue);
    }

    @Transactional
    public void deleteIssue(String projectId, String issueId) {
        IssueContext context = issueAccessService.loadIssueContext(projectId, issueId);
        WorkspaceMember member = context.member();
        issueAuthorizationService.requireManager(member);
        Issue issue = context.issue();
        activityService.issueDeleted(issue, member.getUser());
        issueRepository.delete(issue);
    }

    @Transactional
    public IssueResponse assignUser(String projectId, String issueId, @Valid AssignIssueRequest request) {
        IssueContext context = issueAccessService.loadIssueContext(projectId, issueId);
        Project project = context.project();
        WorkspaceMember member = context.member();
        UUID workspaceUUID = project.getWorkspace().getId();

        UUID assigneeId = request.getUserId();
        WorkspaceMember assigneeMembership = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceUUID, assigneeId)
                .orElseThrow(() -> new BusinessException(
                        "User is not a member of this workspace",
                        ErrorCode.NOT_FOUND
                ));

        issueAuthorizationService.requireContributor(member);
        issueAuthorizationService.requireContributor(assigneeMembership);
        Issue issue = context.issue();
        validationService.validateAssignee(issue, assigneeId);
        issue.setAssignee(assigneeMembership.getUser());
        issueWatcherService.addAssigneeWatcher(issue,assigneeMembership.getUser());
        activityService.issueAssigned(issue, member.getUser(), assigneeMembership.getUser());
        eventPublisher.publishEvent(new IssueAssignedEvent(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                member.getUser().getId(),
                assigneeMembership.getUser().getId(),
                workspaceUUID,
                issue.getProject().getWorkspace().getName(),
                issue.getProject().getId(),
                issue.getProject().getName()
        ));
        return mapper.toResponse(issue);
    }

    @Transactional
    public IssueResponse changePriority(String projectId, String issueId, ChangePriorityRequest request) {
        IssueContext context = issueAccessService.loadIssueContext(projectId, issueId);
        WorkspaceMember member = context.member();
        issueAuthorizationService.requireContributor(member);
        Issue issue = context.issue();
        IssuePriority oldPriority = issue.getPriority();
        validationService.validatePriorityChange(issue, request.getIssuePriority());
        issue.setPriority(request.getIssuePriority());
        activityService.issuePriorityChanged(issue, member.getUser(), oldPriority);
        eventPublisher.publishEvent(new IssuePriorityChangedEvent(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                member.getUser().getId(),
                oldPriority,
                request.getIssuePriority(),
                issue.getProject().getWorkspace().getId(),
                issue.getProject().getWorkspace().getName(),
                issue.getProject().getId(),
                issue.getProject().getName()
        ));
        return mapper.toResponse(issue);
    }


}
