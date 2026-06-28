package org.devsync.spring.issue.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.constants.AppConstants;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.dashboard.dto.ProjectStatsResponse;
import org.devsync.spring.issue.dto.*;
import org.devsync.spring.issue.entity.IssuePriority;
import org.devsync.spring.issue.entity.IssueStatus;
import org.devsync.spring.issue.service.IssueService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Issues")
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/issues")
public class IssueController {

    private final IssueService issueService;

    @Operation(summary = "Create Issue")
    @PostMapping
    public ApiResponse<IssueResponse> createIssue(@PathVariable String projectId,
                                                  @Valid @RequestBody CreateIssueRequest request) {
        IssueResponse response = issueService.createIssue(projectId, request);
        return ApiResponseUtil.success(response, "Issue creation successful");
    }

    @Operation(summary = "Get All Issues")
    @GetMapping
    public ApiResponse<Page<IssueResponse>> getAllIssues(@PathVariable String projectId,
                                                         @RequestParam(required = false)
                                                         IssueStatus status,
                                                         @RequestParam(required = false)
                                                         IssuePriority priority,
                                                         @RequestParam(required = false)
                                                         UUID assigneeId,
                                                         @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE + "") int page,
                                                         @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE + "") int size
    ) {
        IssueFilterRequest filterRequest = IssueFilterRequest.builder().status(status).priority(priority).assigneeId(assigneeId).build();
        Page<IssueResponse> responses = issueService.getAllIssues(projectId, page, size, filterRequest);
        return ApiResponseUtil.success(responses);
    }

    @Operation(summary = "Get Assigned Issue")
    @GetMapping("/my")
    public ApiResponse<Page<IssueResponse>> getMyAssignedIssues(@PathVariable String projectId,
                                                                @RequestParam(required = false)
                                                                IssueStatus status,
                                                                @RequestParam(required = false)
                                                                IssuePriority priority,
                                                                @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE + "") int page,
                                                                @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE + "") int size
    ) {
        Page<IssueResponse> responses = issueService.getMyAssignedIssues(projectId, status, priority, page, size);
        return ApiResponseUtil.success(responses);
    }

    @Operation(summary = "Get Issue By Id")
    @GetMapping("/{issueId}")
    public ApiResponse<IssueResponse> getIssueById(@PathVariable String projectId, @PathVariable String issueId) {
        IssueResponse response = issueService.getIssueById(projectId, issueId);
        return ApiResponseUtil.success(response);
    }

    @Operation(summary = "Update Issue")
    @PutMapping("/{issueId}")
    public ApiResponse<IssueResponse> updateIssue(@PathVariable String projectId, @PathVariable String issueId,
                                                  @Valid @RequestBody UpdateIssueRequest request) {
        IssueResponse response = issueService.updateIssue(projectId, issueId, request);
        return ApiResponseUtil.success(response, "Issue updated successful");
    }

    @Operation(summary = "Update Status")
    @PatchMapping("/{issueId}/status")
    public ApiResponse<IssueResponse> updateStatus(@PathVariable String projectId, @PathVariable String issueId,
                                                   @Valid @RequestBody UpdateStatusRequest request) {
        IssueResponse response = issueService.updateStatus(projectId, issueId, request);
        return ApiResponseUtil.success(response, "Status updated successful");
    }

    @Operation(summary = "Assign User")
    @PatchMapping("/{issueId}/assignee")
    public ApiResponse<IssueResponse> assignUser(@PathVariable String projectId, @PathVariable String issueId, @Valid @RequestBody AssignIssueRequest request) {
        IssueResponse response = issueService.assignUser(projectId, issueId, request);
        return ApiResponseUtil.success(response, "User assigned to Issue");
    }

    @Operation(summary = "Update Priority")
    @PatchMapping("/{issueId}/priority")
    private ApiResponse<IssueResponse> updatePriority(@PathVariable String projectId, @PathVariable String issueId,
                                                      @Valid @RequestBody ChangePriorityRequest request) {
        IssueResponse response = issueService.changePriority(projectId, issueId, request);
        return ApiResponseUtil.success(response, "Priority change successful");
    }

    @Operation(summary = "Delete Issue")
    @DeleteMapping("{issueId}")
    public ApiResponse<Void> deleteIssue(@PathVariable String projectId, @PathVariable String issueId) {
        issueService.deleteIssue(projectId, issueId);
        return ApiResponseUtil.success("Issue deletion successful");
    }


}
