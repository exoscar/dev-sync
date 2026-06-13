package org.devsync.spring.issue.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.activity.entity.ActivityType;
import org.devsync.spring.activity.service.IssueActivityService;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.common.util.Utils;
import org.devsync.spring.issue.dto.*;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.entity.IssueStatus;
import org.devsync.spring.issue.repository.IssueRepository;
import org.devsync.spring.project.entity.Project;
import org.devsync.spring.project.repository.ProjectRepository;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.entity.WorkspaceRole;
import org.devsync.spring.workspace.repository.WorkspaceMemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final CurrentUserService currentUserService;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final IssueActivityService issueActivityService;

    @Transactional
    public IssueResponse createIssue(String projectId, @Valid CreateIssueRequest request) {
        UUID projectUUID = parseProjectId(projectId);
        Project project = getProjectById(projectUUID);
        WorkspaceMember member = getCurrentProjectMember(project);
        if (!canCreateIssue(member.getRole())) {
            throw new BusinessException("Access Denied: You cannot create issue", ErrorCode.FORBIDDEN);
        }
        Issue issue = new Issue();
        issue.setTitle(request.getTitle().trim());
        issue.setDescription(request.getDescription().trim());
        issue.setStatus(IssueStatus.TODO);
        issue.setProject(project);
        issueRepository.save(issue);

        issueActivityService.recordActivity(issue,
                member.getUser(),
                ActivityType.ISSUE_CREATED,
                "Issue Created: "+issue.getTitle());

        return mapToIssueResponse(issue);
    }

    public Page<IssueResponse> getAllIssues(String projectId, int page, int size) {
        UUID projectUUID = parseProjectId(projectId);
        getCurrentProjectMember(projectUUID);
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Issue> issues = issueRepository.findByProjectId(projectUUID, pageable);
        return issues.map(this::mapToIssueResponse);
    }

    @Transactional
    public IssueResponse updateIssue(String projectId, String issueId, UpdateIssueRequest request) {
        UUID projectUUID = parseProjectId(projectId);
        UUID issueUUID = parseIssueId(issueId);
        WorkspaceMember member = getCurrentProjectMember(projectUUID);
        if (!canUpdateIssue(member.getRole())) {
            throw new BusinessException("Access Denied: You can not update issue", ErrorCode.FORBIDDEN);
        }
        Issue issue = getIssue(projectUUID, issueUUID);
        issue.setTitle(request.getTitle().trim());
        issue.setDescription(request.getDescription().trim());
        issueActivityService.recordActivity(issue,
                member.getUser(),
                ActivityType.ISSUE_UPDATED,
                "Issue updated: "+issue.getTitle());
        return mapToIssueResponse(issue);
    }

    public IssueResponse getIssueById(String projectId, String issueId) {
        UUID projectUUID = parseProjectId(projectId);
        UUID issueUUID = parseIssueId(issueId);
        getCurrentProjectMember(projectUUID);
        Issue issue = getIssue(projectUUID, issueUUID);
        return mapToIssueResponse(issue);
    }

    @Transactional
    public IssueResponse updateStatus(String projectId, String issueId, @Valid UpdateStatusRequest request) {
        UUID projectUUID = parseProjectId(projectId);
        UUID issueUUID = parseIssueId(issueId);
        WorkspaceMember member = getCurrentProjectMember(projectUUID);
        if (!canUpdateIssue(member.getRole())) {
            throw new BusinessException("Access Denied: You can not update issue status", ErrorCode.FORBIDDEN);
        }
        Issue issue = getIssue(projectUUID, issueUUID);
        if (issue.getStatus() == request.getStatus()) {
            throw new BusinessException("Status can not be same", ErrorCode.BAD_REQUEST);
        }
        IssueStatus oldStatus = issue.getStatus();
        issue.setStatus(request.getStatus());
        issueActivityService.recordActivity(issue,
                member.getUser(),
                ActivityType.ISSUE_STATUS_CHANGED,
                "Changed status from " + oldStatus+
                        " to " + request.getStatus());
        return mapToIssueResponse(issue);
    }

    @Transactional
    public void deleteIssue(String projectId, String issueId) {
        UUID projectUUID = parseProjectId(projectId);
        UUID issueUUID = parseIssueId(issueId);
        WorkspaceMember member = getCurrentProjectMember(projectUUID);
        if (!canDeleteIssue(member.getRole())) {
            throw new BusinessException("Access Denied: You can not delete issue", ErrorCode.FORBIDDEN);
        }
        Issue issue = getIssue(projectUUID, issueUUID);
        issueActivityService.recordActivity(issue,
                member.getUser(),
                ActivityType.ISSUE_DELETED,
                "Issue Deleted: "+issue.getTitle());
        issueRepository.delete(issue);
    }

    private Issue getIssue(UUID projectUUID, UUID issueUUID) {
        return issueRepository.findByProjectIdAndId(projectUUID, issueUUID).orElseThrow(
                () -> new BusinessException("Issue not found", ErrorCode.NOT_FOUND)
        );
    }

    @Transactional
    public IssueResponse assignUser(String projectId, String issueId, @Valid AssignIssueRequest request) {
        UUID projectUUID = parseProjectId(projectId);
        UUID issueUUID = parseIssueId(issueId);
        UUID assigneeId = request.getUserId();
        Project project = getProjectById(projectUUID);
        UUID workspaceUUID = project.getWorkspace().getId();
        WorkspaceMember member = getCurrentProjectMember(project);
        WorkspaceMember assigneeMembership = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceUUID, assigneeId)
                .orElseThrow(() -> new BusinessException(
                        "User is not a member of this workspace",
                        ErrorCode.NOT_FOUND
                ));
        if (!canAssignUser(member.getRole())) {
            throw new BusinessException("Access Denied: You can not assign user", ErrorCode.FORBIDDEN);
        }
        if (!canManageAssignments(assigneeMembership.getRole())) {
            throw new BusinessException("Access Denied: Assignee do not have enough privileges", ErrorCode.FORBIDDEN);
        }

        Issue issue = getIssue(projectUUID, issueUUID);
        if (issue.getAssignee() != null && issue.getAssignee().getId().equals(assigneeId)) {
            throw new BusinessException(
                    "Issue already assigned to this user",
                    ErrorCode.BAD_REQUEST
            );
        }
        issue.setAssignee(assigneeMembership.getUser());

        issueActivityService.recordActivity(issue,
                member.getUser(),
                ActivityType.ISSUE_ASSIGNED,
                "Issue Assigned to: "+assigneeMembership.getUser().getEmail());

        return mapToIssueResponse(issue);
    }


    private IssueResponse mapToIssueResponse(Issue issue) {
        User assignee = issue.getAssignee();
        return IssueResponse.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .projectId(issue.getProject().getId())
                .projectName(issue.getProject().getName())
                .assigneeId(
                        assignee != null ? assignee.getId() : null
                )
                .assigneeEmail(
                        assignee != null ? assignee.getEmail() : null
                )
                .build();
    }

    private UUID parseProjectId(String id) {
        return Utils.parseUuid(id, "Invalid Project Id");
    }

    private UUID parseIssueId(String id) {
        return Utils.parseUuid(id, "Invalid Issue Id");
    }

    private Project getProjectById(UUID projectUUID) {
        return projectRepository.findById(projectUUID).orElseThrow(
                () -> new BusinessException("Project not found", ErrorCode.NOT_FOUND)
        );
    }

    private boolean canCreateIssue(WorkspaceRole role) {
        return role != WorkspaceRole.VIEWER;
    }

    private boolean canUpdateIssue(WorkspaceRole role) {
        return role != WorkspaceRole.VIEWER;
    }

    private boolean canDeleteIssue(WorkspaceRole role) {
        return role == WorkspaceRole.OWNER || role == WorkspaceRole.MAINTAINER;
    }

    private boolean canAssignUser(WorkspaceRole role) {
        return role != WorkspaceRole.VIEWER;
    }

    private boolean canManageAssignments(WorkspaceRole role) {
        return role != WorkspaceRole.VIEWER;
    }

    private WorkspaceMember getCurrentProjectMember(UUID projectId) {
        return getCurrentProjectMember(
                getProjectById(projectId)
        );
    }

    private WorkspaceMember getCurrentProjectMember(Project project) {
        UUID currUser = currentUserService.getCurrentUserId();
        Workspace workspace = project.getWorkspace();
        return workspaceMemberRepository.findByWorkspaceIdAndUserId(workspace.getId(), currUser).orElseThrow(
                () -> new BusinessException("Access Denied: You do not have access to this workspace", ErrorCode.FORBIDDEN)
        );
    }
}
