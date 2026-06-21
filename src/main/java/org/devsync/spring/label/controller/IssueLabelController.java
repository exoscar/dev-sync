package org.devsync.spring.label.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.label.dto.AddIssueLabelRequest;
import org.devsync.spring.label.dto.IssueLabelResponse;
import org.devsync.spring.label.service.IssueLabelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issues/{issueId}/labels")
@RequiredArgsConstructor
public class IssueLabelController {
    private final IssueLabelService issueLabelService;

    @PostMapping
    public ApiResponse<IssueLabelResponse> addLabel(@PathVariable String issueId,
                                                    @Valid @RequestBody AddIssueLabelRequest request){
        IssueLabelResponse response = issueLabelService.addLabel(issueId,request);
        return ApiResponseUtil.success(response,"label assigned");
    }

    @GetMapping
    public ApiResponse<List<IssueLabelResponse>> getLabels(@PathVariable String issueId){
        List<IssueLabelResponse> responses = issueLabelService.getLabels(issueId);
        return ApiResponseUtil.success(responses);
    }

    @DeleteMapping("/{labelId}")
    public ApiResponse<Void> removeLabel(@PathVariable String issueId,@PathVariable String labelId){
        issueLabelService.removeLabel(issueId,labelId);
        return ApiResponseUtil.success("label removed");
    }


}
