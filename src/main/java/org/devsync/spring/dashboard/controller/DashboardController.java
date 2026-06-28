package org.devsync.spring.dashboard.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.dashboard.dto.MemberStatisticsResponse;
import org.devsync.spring.dashboard.dto.ProjectStatsResponse;
import org.devsync.spring.dashboard.dto.WorkspaceDashboardResponse;
import org.devsync.spring.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Dashboard")
@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Get Workspace Dashboard")
    @GetMapping("/workspaces/{workspaceId}/dashboard")
    public ApiResponse<WorkspaceDashboardResponse> getDashboard(@PathVariable String workspaceId) {
        WorkspaceDashboardResponse response = dashboardService.getWorkspaceDashboard(workspaceId);
        return ApiResponseUtil.success(response);
    }

    @Operation(summary = "Get Project Stats")
    @GetMapping("/projects/{projectId}/dashboard")
    public ApiResponse<ProjectStatsResponse> getProjectStats(@PathVariable String projectId) {
        ProjectStatsResponse statsResponse = dashboardService.getProjectDashboard(projectId);
        return ApiResponseUtil.success(statsResponse);
    }

    @Operation(summary = "Get Member Stats")
    @GetMapping("/workspaces/{workspaceId}/member-statistics")
    public ApiResponse< List<MemberStatisticsResponse>> getMemberStatistics(@PathVariable String workspaceId){
        List<MemberStatisticsResponse> statisticsResponse = dashboardService.getMemberStatistics(workspaceId);
        return ApiResponseUtil.success(statisticsResponse);
    }


}
