package org.devsync.spring.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.dashboard.dto.PrioritySummary;
import org.devsync.spring.dashboard.dto.ProjectStatsResponse;
import org.devsync.spring.dashboard.dto.StatusSummary;
import org.devsync.spring.dashboard.dto.WorkspaceDashboardResponse;
import org.devsync.spring.dashboard.mapper.DashboardMapper;
import org.devsync.spring.issue.projection.IssuePriorityCountProjection;
import org.devsync.spring.issue.projection.IssueStatusCountProjection;
import org.devsync.spring.project.service.ProjectAccessService;
import org.devsync.spring.project.service.ProjectValidationService;
import org.devsync.spring.workspace.service.WorkspaceAccessService;
import org.devsync.spring.workspace.service.WorkspaceValidationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final WorkspaceAccessService workspaceAccessService;
    private final WorkspaceValidationService workspaceValidationService;
    private final DashboardQueryService dashboardQueryService;
    private final ProjectValidationService projectValidationService;
    private final DashboardMapper dashboardMapper;
    private final ProjectAccessService projectAccessService;

    public WorkspaceDashboardResponse getWorkspaceDashboard(String workspaceId) {
        UUID workspaceUUID = workspaceValidationService.parseWorkspaceId(workspaceId);
        workspaceAccessService.getWorkspaceWithMembershipCheck(workspaceUUID);
        long totalProjects = dashboardQueryService.countProjects(workspaceUUID);
        long totalMembers = dashboardQueryService.countMembers(workspaceUUID);
        long totalIssues = dashboardQueryService.countIssuesByWorkspaceId(workspaceUUID);
        List<IssueStatusCountProjection> statusCounts = dashboardQueryService.getIssueStatusCountsByWorkspaceId(workspaceUUID);
        StatusSummary statusSummary = dashboardMapper.toStatusSummary(statusCounts);
        return WorkspaceDashboardResponse.builder()
                .totalProjects(totalProjects)
                .totalMembers(totalMembers)
                .totalIssues(totalIssues)
                .todoIssues(statusSummary.getTodo())
                .inProgressIssues(statusSummary.getInProgress())
                .doneIssues(statusSummary.getDone())
                .build();
    }

    public ProjectStatsResponse getProjectDashboard(String projectId) {
        UUID projectUUID = projectValidationService.parseProjectId(projectId);
        projectAccessService.getProjectWithMembershipCheck(projectUUID);
        List<IssueStatusCountProjection> statusCounts = dashboardQueryService.getIssueStatusCountsByProjectId(projectUUID);
        long totalIssues = dashboardQueryService.countIssuesByProjectId(projectUUID);
        StatusSummary statusSummary = dashboardMapper.toStatusSummary(statusCounts);

        List<IssuePriorityCountProjection> priorityCounts = dashboardQueryService.getIssuePriorityCounts(projectUUID);
        PrioritySummary prioritySummary = dashboardMapper.toPrioritySummary(priorityCounts);
        long assignedIssues = dashboardQueryService.countAssignedIssues(projectUUID);
        long unassignedIssues = dashboardQueryService.countUnassignedIssues(projectUUID);

        return ProjectStatsResponse.builder()
                .totalIssues(totalIssues)
                .todoIssues(statusSummary.getTodo())
                .inProgressIssues(statusSummary.getInProgress())
                .doneIssues(statusSummary.getDone())
                .lowPriority(prioritySummary.getLow())
                .mediumPriority(prioritySummary.getMedium())
                .highPriority(prioritySummary.getHigh())
                .criticalPriority(prioritySummary.getCritical())
                .assignedIssues(assignedIssues)
                .unassignedIssues(unassignedIssues).build();
    }


}