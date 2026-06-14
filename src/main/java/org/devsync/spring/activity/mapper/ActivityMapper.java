package org.devsync.spring.activity.mapper;

import org.devsync.spring.activity.dto.ActivityFeedResponse;
import org.devsync.spring.activity.dto.IssueActivityResponse;
import org.devsync.spring.activity.entity.IssueActivity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {
    public IssueActivityResponse mapToIssueActivityResponse(IssueActivity activity) {
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

    public ActivityFeedResponse mapToActivityFeedResponse(IssueActivity activity) {
        return ActivityFeedResponse
                .builder()
                .description(activity.getDescription())
                .activityType(activity.getActivityType())
                .issueId(activity.getIssue().getId())
                .issueTitle(activity.getIssue().getTitle())
                .performedById(activity.getUser().getId())
                .performedByName(activity.getUser().getFirstName())
                .createdAt(activity.getCreatedAt())
                .build();
    }
}
