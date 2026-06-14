package org.devsync.spring.activity.dto;


import lombok.Builder;
import lombok.Data;
import org.devsync.spring.activity.entity.ActivityType;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ActivityFeedResponse {

    private ActivityType activityType;

    private UUID issueId;

    private String issueTitle;
    private String description;

    private UUID performedById;

    private String performedByName;

    private Instant createdAt;
}