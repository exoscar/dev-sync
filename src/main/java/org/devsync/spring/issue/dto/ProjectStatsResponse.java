package org.devsync.spring.issue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStatsResponse {

    private long totalIssues;

    private long todoIssues;
    private long inProgressIssues;
    private long doneIssues;

    private long lowPriority;
    private long mediumPriority;
    private long highPriority;
    private long criticalPriority;

    private long assignedIssues;
    private long unassignedIssues;
}
