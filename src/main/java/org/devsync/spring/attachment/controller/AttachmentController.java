package org.devsync.spring.attachment.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.attachment.dto.AttachmentDownloadResponse;
import org.devsync.spring.attachment.dto.AttachmentResponse;
import org.devsync.spring.attachment.service.AttachmentService;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Attachments")
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/issues/{issueId}/attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @Operation(summary = "Upload File")
    @PostMapping
    public ApiResponse<AttachmentResponse> uploadFile(@PathVariable String projectId,
                                                      @PathVariable String issueId,
                                                      @RequestParam("file")MultipartFile file
    ){
    AttachmentResponse response = attachmentService.uploadFile(projectId,issueId,file);
    return ApiResponseUtil.success(response);
    }

    @Operation(summary = "Attachment list")
    @GetMapping
    public ApiResponse<List<AttachmentResponse>>
    getAttachments(
            @PathVariable String projectId,
            @PathVariable String issueId
    ){
        List<AttachmentResponse> responses = attachmentService.getAttachments(projectId,issueId);
        return ApiResponseUtil.success(responses);
    }

    @Operation(summary = "Download Attachment")
    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<Resource>
    downloadAttachment(
            @PathVariable String projectId,
            @PathVariable String issueId,
            @PathVariable String attachmentId
    ){
        AttachmentDownloadResponse response = attachmentService.getAttachmentResource(projectId,issueId,attachmentId);
        return ApiResponseUtil.download(response.getResource(), response.getOriginalFilename(), response.getContentType());
    }

    @Operation(summary = "Delete Attachment")
    @DeleteMapping("/{attachmentId}")
    public ApiResponse<Void>
    deleteAttachment(
            @PathVariable String projectId,
            @PathVariable String issueId,
            @PathVariable String attachmentId
    ){
         attachmentService.deleteAttachment(projectId,issueId,attachmentId);
     return ApiResponseUtil.success("Attachment deleted successfully");
       }


}
