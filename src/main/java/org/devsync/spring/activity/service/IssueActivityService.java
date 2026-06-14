package org.devsync.spring.activity.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.activity.dto.ActivityFeedResponse;
import org.devsync.spring.activity.dto.IssueActivityResponse;
import org.devsync.spring.activity.entity.ActivityType;
import org.devsync.spring.activity.entity.IssueActivity;
import org.devsync.spring.activity.mapper.ActivityMapper;
import org.devsync.spring.activity.repository.IssueActivityRepository;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.issue.context.IssueContext;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.service.IssueAccessService;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.service.WorkspaceAccessService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueActivityService {
    private final IssueActivityRepository issueActivityRepository;
    private final WorkspaceAccessService workspaceAccessService;
    private final ActivityMapper activityMapper;
    private final IssueAccessService issueAccessService;

    @Transactional
    public void recordActivity(Issue issue, User user, ActivityType activityType, String description){
        IssueActivity activity = new IssueActivity();
        activity.setIssue(issue);
        activity.setUser(user);
        activity.setActivityType(activityType);
        activity.setDescription(description);
        issueActivityRepository.save(activity);
    }
    public Page<IssueActivityResponse> getIssueActivities(String issueId, int page, int size) {
        IssueContext context = issueAccessService.loadIssueContext(issueId);
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdAt"));
        Page<IssueActivity> issueActivities = issueActivityRepository.findByIssueId(context.issue().getId(), pageable);
        return issueActivities.map(activityMapper::mapToIssueActivityResponse);
    }

    public Page<ActivityFeedResponse> getWorkspaceActivityFeed(String workspaceId, int page, int size) {
        Workspace workspace = workspaceAccessService.getWorkspaceWithMembershipCheck(workspaceId);
        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdAt"));
        Page<IssueActivity> activities = issueActivityRepository.findAllByIssueProjectWorkspaceId(
                workspace.getId(),
                pageable
        );
        return activities.map(activityMapper::mapToActivityFeedResponse);
    }
}
