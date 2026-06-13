package org.devsync.spring.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devsync.spring.activity.entity.ActivityType;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueActivityResponse {
    private UUID id;
    private UUID issueId;
    private UUID userId;
    private String email;
    private ActivityType activityType;
    private String description;
    private Instant createdAt;
}
