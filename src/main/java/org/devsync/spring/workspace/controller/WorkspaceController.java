package org.devsync.spring.workspace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.constants.AppConstants;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.workspace.dto.*;
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

    @PostMapping("/{id}/members")
    public ApiResponse<MemberResponse> inviteWorkspaceMember(@PathVariable String id, @Valid @RequestBody InviteMemberRequest request){
        MemberResponse response = workspaceService.inviteMember(id,request);
        return ApiResponseUtil.success(response,"User invited");
    }

    @GetMapping("/{id}/members")
    public ApiResponse<List<WorkspaceMemberResponse>> getWorkspaceMembers(@PathVariable String id){
        List<WorkspaceMemberResponse> response = workspaceService.getWorkspaceMembers(id);
        return ApiResponseUtil.success(response);
    }

    @PutMapping("/{id}/members/role")
    public ApiResponse<MemberResponse> updateRole(@PathVariable String id,@Valid @RequestBody UpdateRoleRequest request){
        MemberResponse response = workspaceService.updateMemberRole(id,request);
        return ApiResponseUtil.success(response,"Member role updated");
    }

    @DeleteMapping("/{id}/members")
    public ApiResponse<Void> deleteMember(@PathVariable String id,@Valid @RequestBody DeleteMemberRequest request){
        workspaceService.deleteMember(id,request);
        return ApiResponseUtil.success("Member Deletion Successful");
    }

}