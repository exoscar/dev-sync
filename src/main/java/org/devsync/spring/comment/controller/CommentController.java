package org.devsync.spring.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.comment.dto.CommentResponse;
import org.devsync.spring.comment.dto.CreateCommentRequest;
import org.devsync.spring.comment.dto.UpdateCommentRequest;
import org.devsync.spring.comment.service.CommentService;
import org.devsync.spring.common.constants.AppConstants;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Create Comment")
    @PostMapping("/issues/{issueId}/comments")
    public ApiResponse<CommentResponse> createComment(@PathVariable String issueId,
                                                      @Valid @RequestBody CreateCommentRequest request){
        CommentResponse response = commentService.createComment(issueId,request);
        return ApiResponseUtil.success(response,"Comment creation successful");
    }

    @Operation(summary = "Get Issue Comments")
    @GetMapping("/issues/{issueId}/comments")
    public ApiResponse<Page<CommentResponse>> getComments(@PathVariable String issueId,
                                                          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE+"") int page,
                                                          @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE+"") int size
    ){
        Page<CommentResponse> responses = commentService.getComments(issueId,page,size);
        return ApiResponseUtil.success(responses);
    }

    @Operation(summary = "Edit Comment")
    @PutMapping("/comments/{commentId}")
    public ApiResponse<CommentResponse> editComment(@PathVariable String commentId,
                                                    @Valid @RequestBody UpdateCommentRequest updateCommentRequest){
        CommentResponse response = commentService.updateComment(commentId,updateCommentRequest);
        return ApiResponseUtil.success(response,"Comment Update successful");
    }

    @Operation(summary = "Delete Comment")
    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> deleteComments(@PathVariable String commentId){
        commentService.deleteComment(commentId);
        return ApiResponseUtil.success("Comment deletion successful");
    }
}
