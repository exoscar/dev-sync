package org.devsync.spring.issue.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.issue.entity.IssuePriority;
import org.devsync.spring.issue.entity.IssueStatus;

import java.util.UUID;

@Data
@Builder
public class IssueFilterRequest {
    private IssueStatus status;
    private IssuePriority priority;
    private UUID assigneeId;


}
