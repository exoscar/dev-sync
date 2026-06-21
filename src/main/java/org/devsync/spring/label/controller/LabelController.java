package org.devsync.spring.label.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.label.dto.CreateLabelRequest;
import org.devsync.spring.label.dto.LabelResponse;
import org.devsync.spring.label.dto.UpdateLabelRequest;
import org.devsync.spring.label.service.LabelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces/{workspaceId}/labels")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;

    @PostMapping
    public ApiResponse<LabelResponse> createLabel(@PathVariable String workspaceId, @Valid @RequestBody CreateLabelRequest request) {
        LabelResponse response = labelService.createLabel(workspaceId, request);
        return ApiResponseUtil.success(response, "Label Created");
    }

    @GetMapping
    public ApiResponse<List<LabelResponse>> getLabels(@PathVariable String workspaceId) {
        List<LabelResponse> responses = labelService.getLabels(workspaceId);
        return ApiResponseUtil.success(responses);
    }

    @GetMapping("/{labelId}")
    public ApiResponse<LabelResponse> getLabelById(@PathVariable String workspaceId, @PathVariable String labelId) {
        LabelResponse response = labelService.getLabelByWorkspaceIdAndId(workspaceId, labelId);
        return ApiResponseUtil.success(response);
    }

    @PutMapping("/{labelId}")
    public ApiResponse<LabelResponse> updateLabel(@PathVariable String workspaceId, @PathVariable String labelId,
                                                  @Valid @RequestBody UpdateLabelRequest request
                                                  ){
        LabelResponse response = labelService.updateLabel(workspaceId,labelId,request);
        return ApiResponseUtil.success(response);
    }

    @DeleteMapping("/{labelId}")
    public ApiResponse<Void> deleteLabel(@PathVariable String workspaceId,@PathVariable String labelId){
        labelService.archiveLabel(workspaceId,labelId);
        return ApiResponseUtil.success("Label deletion successful");
    }

}

