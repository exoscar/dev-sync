package org.devsync.spring.activity.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.activity.dto.IssueActivityResponse;
import org.devsync.spring.activity.service.IssueActivityService;
import org.devsync.spring.common.constants.AppConstants;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IssueActivityController {
    private final IssueActivityService issueActivityService;

    @GetMapping("/issues/{issueId}/activities")
    public ApiResponse<Page<IssueActivityResponse>> getIssueActivity(@PathVariable String issueId,
                                                                     @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE+"") int page,
                                                                     @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE+"") int size){
        Page<IssueActivityResponse> responses = issueActivityService.getIssueActivity(issueId,page,size);
        return ApiResponseUtil.success(responses);
    }
}
