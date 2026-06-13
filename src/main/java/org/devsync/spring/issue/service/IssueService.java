package org.devsync.spring.issue.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.common.util.Utils;
import org.devsync.spring.issue.dto.CreateIssueRequest;
import org.devsync.spring.issue.dto.IssueResponse;
import org.devsync.spring.issue.dto.UpdateIssueRequest;
import org.devsync.spring.issue.dto.UpdateStatusRequest;
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

    @Transactional
    public IssueResponse createIssue(String projectId, @Valid CreateIssueRequest request) {
        UUID projectUUID = parseProjectId(projectId);
        Project project = getProjectById(projectUUID);
        WorkspaceMember member = getCurrentProjectMember(project);
        if(!canCreateIssue(member.getRole())){
            throw new BusinessException("Access Denied: You cannot create issue",ErrorCode.FORBIDDEN);
        }
        Issue issue =  new Issue();
        issue.setTitle(request.getTitle().trim());
        issue.setDescription(request.getDescription().trim());
        issue.setStatus(IssueStatus.TODO);
        issue.setProject(project);
        issueRepository.save(issue);
        return mapToIssueResponse(issue);
    }

    public Page<IssueResponse> getAllIssues(String projectId,int page,int size) {
        UUID projectUUID = parseProjectId(projectId);
        getCurrentProjectMember(projectUUID);
        PageRequest pageable = PageRequest.of(page,size, Sort.by("id"));
        Page<Issue> issues = issueRepository.findByProjectId(projectUUID,pageable);

        return issues.map(this::mapToIssueResponse);
    }

    public IssueResponse getIssueById(String projectId, String issueId) {
        UUID projectUUID = parseProjectId(projectId);
        UUID issueUUID = parseIssueId(issueId);
        getCurrentProjectMember(projectUUID);
        Issue issue = issueRepository.findByProjectIdAndId(projectUUID,issueUUID).orElseThrow(
                ()-> new BusinessException("Issue not found",ErrorCode.NOT_FOUND)
        );
        return mapToIssueResponse(issue);
    }
    @Transactional
    public IssueResponse updateIssue(String projectId, String issueId, UpdateIssueRequest request) {
        UUID projectUUID = parseProjectId(projectId);
        UUID issueUUID = parseIssueId(issueId);
        WorkspaceMember member = getCurrentProjectMember(projectUUID);
        if(!canUpdateIssue(member.getRole())){
            throw new BusinessException("Access Denied: You can not update issue",ErrorCode.FORBIDDEN);
        }
        Issue issue = issueRepository.findByProjectIdAndId(projectUUID,issueUUID).orElseThrow(
                ()-> new BusinessException("Issue not found",ErrorCode.NOT_FOUND)
        );
        issue.setTitle(request.getTitle().trim());
        issue.setDescription(request.getDescription().trim());
        return mapToIssueResponse(issue);
    }

    @Transactional
    public IssueResponse updateStatus(String projectId, String issueId, @Valid UpdateStatusRequest request) {
        UUID projectUUID = parseProjectId(projectId);
        UUID issueUUID = parseIssueId(issueId);
        WorkspaceMember member = getCurrentProjectMember(projectUUID);
        if(!canUpdateIssue(member.getRole())){
            throw new BusinessException("Access Denied: You can not update issue status",ErrorCode.FORBIDDEN);
        }
        Issue issue = issueRepository.findByProjectIdAndId(projectUUID,issueUUID).orElseThrow(
                ()-> new BusinessException("Issue not found",ErrorCode.NOT_FOUND)
        );
        if(issue.getStatus() == request.getStatus()){
            throw new BusinessException("Status can not be same",ErrorCode.BAD_REQUEST);
        }
        issue.setStatus(request.getStatus());
        return mapToIssueResponse(issue);
    }

    @Transactional
    public void deleteIssue(String projectId, String issueId) {
        UUID projectUUID = parseProjectId(projectId);
        UUID issueUUID = parseIssueId(issueId);
        WorkspaceMember member = getCurrentProjectMember(projectUUID);
        if(!canDeleteIssue(member.getRole())){
            throw new BusinessException("Access Denied: You can not delete issue",ErrorCode.FORBIDDEN);
        }
        Issue issue = issueRepository.findByProjectIdAndId(projectUUID,issueUUID).orElseThrow(
                ()-> new BusinessException("Issue not found",ErrorCode.NOT_FOUND)
        );
        issueRepository.delete(issue);
    }


    private IssueResponse mapToIssueResponse(Issue issue){
        return IssueResponse.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .projectId(issue.getProject().getId())
                .projectName(issue.getProject().getName())
                .build();
    }
    private UUID parseProjectId(String id){
        return Utils.parseUuid(id,"Invalid Project Id");
    }
    private UUID parseIssueId(String id){
        return Utils.parseUuid(id,"Invalid Issue Id");
    }
    private Project getProjectById(UUID projectUUID){
        return projectRepository.findById(projectUUID).orElseThrow(
                ()-> new BusinessException("Project not found", ErrorCode.NOT_FOUND)
        );
    }

    private boolean canCreateIssue(WorkspaceRole role){
        return role != WorkspaceRole.VIEWER;
    }

    private boolean canUpdateIssue(WorkspaceRole role) {return role != WorkspaceRole.VIEWER;}

    private boolean canDeleteIssue(WorkspaceRole role){
        return role==WorkspaceRole.OWNER || role == WorkspaceRole.MAINTAINER;
    }

    private WorkspaceMember getCurrentProjectMember(UUID projectId) {
        return getCurrentProjectMember(
                getProjectById(projectId)
        );
    }

    private WorkspaceMember getCurrentProjectMember(Project project){
        UUID currUser = currentUserService.getCurrentUserId();
        Workspace workspace = project.getWorkspace();
        return workspaceMemberRepository.findByWorkspaceIdAndUserId(workspace.getId(),currUser).orElseThrow(
                ()->new BusinessException("Access Denied: You do not have access to this workspace",ErrorCode.FORBIDDEN)
        );
    }
}
