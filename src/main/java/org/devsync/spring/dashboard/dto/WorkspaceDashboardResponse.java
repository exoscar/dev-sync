package org.devsync.spring.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDashboardResponse {
    private long totalProjects;

    private long totalMembers;

    private long totalIssues;

    private long todoIssues;

    private long inProgressIssues;

    private long doneIssues;
}
