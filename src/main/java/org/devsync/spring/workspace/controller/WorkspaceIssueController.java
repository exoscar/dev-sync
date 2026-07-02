package org.devsync.spring.workspace.controller;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.constants.AppConstants;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.issue.dto.IssueResponse;
import org.devsync.spring.workspace.dto.WorkspaceIssueFilterRequest;
import org.devsync.spring.workspace.service.WorkspaceIssueService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workspaces/{workspaceId}/issues")
@RequiredArgsConstructor
public class WorkspaceIssueController {

    private final WorkspaceIssueService workspaceIssueService;

    @GetMapping
    public ApiResponse<Page<IssueResponse>> getWorkspaceIssues(
            @PathVariable UUID workspaceId,
            @ModelAttribute WorkspaceIssueFilterRequest request,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE + "") int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE + "") int size
    ) {
        Page<IssueResponse> response =
                workspaceIssueService.filter(
                        workspaceId,
                        request,
                        page,
                        size
                );

        return ApiResponseUtil.success(response);
    }
}
