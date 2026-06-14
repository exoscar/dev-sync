package org.devsync.spring.issue.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.activity.entity.ActivityType;
import org.devsync.spring.activity.service.IssueActivityService;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.entity.IssuePriority;
import org.devsync.spring.issue.entity.IssueStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueActivityUtilService {
    private final IssueActivityService activityService;

    public void issueCreated(Issue issue, User actor) {
        activityService.recordActivity(
                issue, actor,
                ActivityType.ISSUE_CREATED,
                "Issue Created: " + issue.getTitle()
        );
    }

    public void issueUpdated(Issue issue, User actor) {
        activityService.recordActivity(issue,
                actor,
                ActivityType.ISSUE_UPDATED,
                "Issue updated: " + issue.getTitle()
        );
    }

    public void issueStatusChanged(Issue issue, User actor, IssueStatus oldStatus) {
        activityService.recordActivity(
                issue,
                actor,
                ActivityType.ISSUE_STATUS_CHANGED,
                "Changed status from " + oldStatus +
                        " to " + issue.getStatus()
        );
    }

    public void issueDeleted(Issue issue, User actor) {
        activityService.recordActivity(issue,
                actor,
                ActivityType.ISSUE_DELETED,
                "Issue Deleted: " + issue.getTitle()
        );
    }

    public void issueAssigned(Issue issue, User actor, User target) {
        activityService.recordActivity(issue,
                actor,
                ActivityType.ISSUE_ASSIGNED,
                "Issue Assigned to: " + target.getEmail()
        );
    }

    public void issuePriorityChanged(Issue issue, User actor, IssuePriority oldPriority) {
        activityService.recordActivity(issue,
                actor,
                ActivityType.ISSUE_PRIORITY_CHANGED,
                "Priority changed from " + oldPriority + " to " + issue.getPriority()
        );
    }


}
