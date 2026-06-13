package org.devsync.spring.issue.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.constants.AppConstants;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.issue.dto.*;
import org.devsync.spring.issue.service.IssueService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/issues")
public class IssueController {

    private final IssueService issueService;

    @PostMapping
    public ApiResponse<IssueResponse> createIssue(@PathVariable String projectId,
                                                  @Valid @RequestBody CreateIssueRequest request){
        IssueResponse response = issueService.createIssue(projectId,request);
        return ApiResponseUtil.success(response,"Issue creation successful");
    }

    @GetMapping
    public ApiResponse<Page<IssueResponse>> getAllIssues(@PathVariable String projectId,
                                                         @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE+"") int page,
                                                         @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE+"") int size
                                                         ){
        Page<IssueResponse> responses =issueService.getAllIssues(projectId,page,size);
        return ApiResponseUtil.success(responses);
    }

    @GetMapping("/{issueId}")
    public ApiResponse<IssueResponse> getIssueById(@PathVariable String projectId,@PathVariable String issueId){
        IssueResponse response = issueService.getIssueById(projectId,issueId);
        return ApiResponseUtil.success(response);
    }

    @PutMapping("/{issueId}")
    public ApiResponse<IssueResponse> updateIssue(@PathVariable String projectId, @PathVariable String issueId,
                                                  @Valid @RequestBody UpdateIssueRequest request){
        IssueResponse response = issueService.updateIssue(projectId,issueId,request);
        return ApiResponseUtil.success(response,"Issue updated successful");
    }

    @PatchMapping("/{issueId}/status")
    public ApiResponse<IssueResponse> updateStatus(@PathVariable String projectId, @PathVariable String issueId,
                                                   @Valid @RequestBody UpdateStatusRequest request){
        IssueResponse response = issueService.updateStatus(projectId,issueId,request);
        return ApiResponseUtil.success(response,"Status updated successful");
    }

    @PatchMapping("/{issueId}/assignee")
    public ApiResponse<IssueResponse> assignUser(@PathVariable String projectId,@PathVariable String issueId,@Valid @RequestBody AssignIssueRequest request){
        IssueResponse response = issueService.assignUser(projectId,issueId,request);
        return ApiResponseUtil.success(response,"User assigned to Issue");
    }

    @DeleteMapping("{issueId}")
    public ApiResponse<Void> deleteIssue(@PathVariable String projectId,@PathVariable String issueId){
         issueService.deleteIssue(projectId,issueId);
        return ApiResponseUtil.success("Issue deletion successful");
    }


}
