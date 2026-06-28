package org.devsync.spring.label.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.label.dto.AddIssueLabelRequest;
import org.devsync.spring.label.dto.IssueLabelResponse;
import org.devsync.spring.label.service.IssueLabelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Issue Labels")
@RestController
@RequestMapping("/issues/{issueId}/labels")
@RequiredArgsConstructor
public class IssueLabelController {
    private final IssueLabelService issueLabelService;

    @Operation(operationId = "Add Label to Issue")
    @PostMapping
    public ApiResponse<IssueLabelResponse> addLabel(@PathVariable String issueId,
                                                    @Valid @RequestBody AddIssueLabelRequest request){
        IssueLabelResponse response = issueLabelService.addLabel(issueId,request);
        return ApiResponseUtil.success(response,"label assigned");
    }

    @Operation(summary = "Get Issue Labels")
    @GetMapping
    public ApiResponse<List<IssueLabelResponse>> getLabels(@PathVariable String issueId){
        List<IssueLabelResponse> responses = issueLabelService.getLabels(issueId);
        return ApiResponseUtil.success(responses);
    }

    @Operation(summary = "Delete Issue Label")
    @DeleteMapping("/{labelId}")
    public ApiResponse<Void> removeLabel(@PathVariable String issueId,@PathVariable String labelId){
        issueLabelService.removeLabel(issueId,labelId);
        return ApiResponseUtil.success("label removed");
    }


}
