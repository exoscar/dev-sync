package org.devsync.spring.issue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devsync.spring.issue.entity.IssuePriority;
import org.devsync.spring.issue.entity.IssueStatus;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueResponse {
    private UUID id;
    private String title;
    private String description;
    private IssueStatus status;
    private IssuePriority priority;

    private UUID projectId;
    private String projectName;

    private UUID assigneeId;
    private String assigneeEmail;
}
