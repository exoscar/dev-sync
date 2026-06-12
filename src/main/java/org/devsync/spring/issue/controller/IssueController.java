package org.devsync.spring.issue.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.constants.AppConstants;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.issue.dto.CreateIssueRequest;
import org.devsync.spring.issue.dto.IssueResponse;
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

}
