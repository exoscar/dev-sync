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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Workspace", description = "Workspace management APIs")
@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    @Operation(
            summary = "Create workspace",
            description = "Create a new workspace for the authenticated user"
    )
    public ApiResponse<WorkspaceResponse> createWorkspace(
            @Valid @RequestBody CreateWorkspaceRequest request) {
        WorkspaceResponse response = workspaceService.createWorkspace(request);
        return ApiResponseUtil.success(response, "Workspace created");
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get workspace",
            description = "Retrieve workspace details by workspace ID"
    )
    public ApiResponse<WorkspaceResponse> getWorkspaceById(@PathVariable String id) {
        WorkspaceResponse response = workspaceService.getWorkspaceById(id);
        return ApiResponseUtil.success(response);
    }

    @GetMapping
    @Operation(
            summary = "Get user workspaces",
            description = "Retrieve all workspaces accessible to the authenticated user"
    )
    public ApiResponse<List<WorkspaceResponse>> getUserWorkspaces() {
        List<WorkspaceResponse> response = workspaceService.getUserWorkspaces();
        return ApiResponseUtil.success(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get all workspaces",
            description = "Retrieve paginated list of all workspaces (Admin only)"
    )
    public ApiResponse<Page<WorkspaceResponse>> getAllWorkspaces(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE + "") int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE + "") int size
    ) {
        Page<WorkspaceResponse> response = workspaceService.getAllWorkspaces(page, size);
        return ApiResponseUtil.success(response);
    }

    @PostMapping("/{id}/members")
    @Operation(
            summary = "Invite member",
            description = "Invite a user to join a workspace"
    )
    public ApiResponse<MemberResponse> inviteWorkspaceMember(
            @PathVariable String id,
            @Valid @RequestBody InviteMemberRequest request) {
        MemberResponse response = workspaceService.inviteMember(id, request);
        return ApiResponseUtil.success(response, "User invited");
    }

    @GetMapping("/{id}/members")
    @Operation(
            summary = "Get workspace members",
            description = "Retrieve all members of a workspace"
    )
    public ApiResponse<List<WorkspaceMemberResponse>> getWorkspaceMembers(
            @PathVariable String id) {
        List<WorkspaceMemberResponse> response = workspaceService.getWorkspaceMembers(id);
        return ApiResponseUtil.success(response);
    }

    @PutMapping("/{id}/members/role")
    @Operation(
            summary = "Update member role",
            description = "Update a workspace member's role"
    )
    public ApiResponse<MemberResponse> updateRole(
            @PathVariable String id,
            @Valid @RequestBody UpdateRoleRequest request) {
        MemberResponse response = workspaceService.updateMemberRole(id, request);
        return ApiResponseUtil.success(response, "Member role updated");
    }

    @DeleteMapping("/{id}/members")
    @Operation(
            summary = "Remove member",
            description = "Remove a member from a workspace"
    )
    public ApiResponse<Void> deleteMember(
            @PathVariable String id,
            @Valid @RequestBody DeleteMemberRequest request) {
        workspaceService.deleteMember(id, request);
        return ApiResponseUtil.success("Member Deletion Successful");
    }
}