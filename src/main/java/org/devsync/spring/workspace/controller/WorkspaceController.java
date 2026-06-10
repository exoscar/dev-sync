package org.devsync.spring.workspace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.constants.AppConstants;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.workspace.dto.CreateWorkspaceRequest;
import org.devsync.spring.workspace.dto.WorkspaceResponse;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.service.WorkspaceService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

     private final WorkspaceService workspaceService;

    @PostMapping
    public ApiResponse<WorkspaceResponse> createWorkspace(@Valid @RequestBody CreateWorkspaceRequest request){
        WorkspaceResponse response = workspaceService.createWorkspace(request);
        return ApiResponseUtil.success(response,"Workspace created");

    }

    @GetMapping("/{id}")
    public ApiResponse<WorkspaceResponse> getWorkspaceById(@PathVariable String id){
        WorkspaceResponse response = workspaceService.getWorkspaceById(id);
        return ApiResponseUtil.success(response);
    }

    @GetMapping
    public ApiResponse<List<WorkspaceResponse>> getUserWorkspaces(){
        List<WorkspaceResponse> response = workspaceService.getUserWorkspaces();
        return ApiResponseUtil.success(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<WorkspaceResponse>> getAllWorkspaces(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE + "") int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE + "") int size
    ){
        Page<WorkspaceResponse> response = workspaceService.getAllWorkspaces(page,size);
        return ApiResponseUtil.success(response);
    }
}
