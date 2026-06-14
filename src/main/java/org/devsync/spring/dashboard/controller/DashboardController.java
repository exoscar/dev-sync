package org.devsync.spring.dashboard.controller;


import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.dashboard.dto.ProjectStatsResponse;
import org.devsync.spring.dashboard.dto.WorkspaceDashboardResponse;
import org.devsync.spring.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/workspaces/{workspaceId}/dashboard")
    public ApiResponse<WorkspaceDashboardResponse> getDashboard(@PathVariable String workspaceId) {
        WorkspaceDashboardResponse response = dashboardService.getWorkspaceDashboard(workspaceId);
        return ApiResponseUtil.success(response);
    }

    @GetMapping("/projects/{projectId}/dashboard")
    public ApiResponse<ProjectStatsResponse> getProjectStats(@PathVariable String projectId) {
        ProjectStatsResponse statsResponse = dashboardService.getProjectDashboard(projectId);
        return ApiResponseUtil.success(statsResponse);
    }


}
