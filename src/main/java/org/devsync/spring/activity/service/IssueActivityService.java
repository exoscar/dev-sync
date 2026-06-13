package org.devsync.spring.activity.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.activity.dto.IssueActivityResponse;
import org.devsync.spring.activity.entity.ActivityType;
import org.devsync.spring.activity.entity.IssueActivity;
import org.devsync.spring.activity.repository.IssueActivityRepository;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.common.util.Utils;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.repository.IssueRepository;
import org.devsync.spring.project.entity.Project;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.repository.WorkspaceMemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueActivityService {
    private final IssueActivityRepository issueActivityRepository;
    private final CurrentUserService currentUserService;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final IssueRepository issueRepository;

    @Transactional
    public void recordActivity(Issue issue, User user, ActivityType activityType, String description){
        IssueActivity activity = new IssueActivity();
        activity.setIssue(issue);
        activity.setUser(user);
        activity.setActivityType(activityType);
        activity.setDescription(description);
        issueActivityRepository.save(activity);
    }


    public Page<IssueActivityResponse> getIssueActivity(String issueId,int page,int size) {
        UUID issueUUID = parseIssueId(issueId);
        Issue issue = getIssue(issueUUID);
        getCurrentProjectMember(issue.getProject());
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdAt"));
        Page<IssueActivity> issueActivities = issueActivityRepository.findByIssueId(issueUUID,pageable);
        return issueActivities.map(this::mapToIssueActivityResponse);
    }

    private IssueActivityResponse mapToIssueActivityResponse(IssueActivity activity){
       return IssueActivityResponse.builder()
               .id(activity.getId())
               .issueId(activity.getIssue().getId())
               .userId(activity.getUser().getId())
               .email(activity.getUser().getEmail())
               .activityType(activity.getActivityType())
               .description(activity.getDescription())
               .createdAt(activity.getCreatedAt())
               .build();
    }

    private WorkspaceMember getCurrentProjectMember(Project project) {
        UUID currUser = currentUserService.getCurrentUserId();
        Workspace workspace = project.getWorkspace();
        return workspaceMemberRepository.findByWorkspaceIdAndUserId(workspace.getId(), currUser).orElseThrow(
                () -> new BusinessException("Access Denied: You do not have access to this workspace", ErrorCode.FORBIDDEN)
        );
    }
    private Issue getIssue(UUID issueId){
        return issueRepository.findById(issueId).orElseThrow(
                () -> new BusinessException("Issue not found", ErrorCode.NOT_FOUND)
        );
    }

    private UUID parseIssueId(String id) {
        return Utils.parseUuid(id, "Invalid Issue Id");
    }
}
