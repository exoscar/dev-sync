package org.devsync.spring.workspace.service;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.auth.repository.UserRepository;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.user.dto.UserResponse;
import org.devsync.spring.workspace.dto.*;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.entity.WorkspaceRole;
import org.devsync.spring.workspace.repository.WorkspaceMemberRepository;
import org.devsync.spring.workspace.repository.WorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final CurrentUserService currentUserService;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;
    private final WorkspaceAccessService workspaceAccessService;
    private final WorkspaceValidationService validationService;

    @Transactional
    public WorkspaceResponse createWorkspace(CreateWorkspaceRequest request) {
        UUID currUser = currentUserService.getCurrentUserId();

        User user = userRepository.findById(currUser).orElseThrow(
                () -> new BusinessException("User not Found", ErrorCode.NOT_FOUND));

        if (workspaceRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessException("Workspace name already exist", ErrorCode.WORKSPACE_NAME_ALREADY_EXISTS);
        }

        Workspace workspace = new Workspace();
        workspace.setName(request.getName());
        workspace.setOwner(user);
        workspaceRepository.save(workspace);

        WorkspaceMember workspaceMember = new WorkspaceMember();
        workspaceMember.setWorkspace(workspace);
        workspaceMember.setRole(WorkspaceRole.OWNER);
        workspaceMember.setUser(user);
        workspaceMemberRepository.save(workspaceMember);
        return mapToWorkspace(workspace);
    }

    public WorkspaceResponse getWorkspaceById(String id) {
        Workspace workspace =  workspaceAccessService.getWorkspaceWithMembershipCheck(id);
        return mapToWorkspace(workspace);
    }

    public List<WorkspaceResponse> getUserWorkspaces() {
        UUID currUser = currentUserService.getCurrentUserId();
        List<Workspace> userMembership = workspaceMemberRepository.findWorkspacesByUserId(currUser);
        return userMembership.stream().map(this::mapToWorkspace).toList();
    }

    public Page<WorkspaceResponse> getAllWorkspaces(int page, int size) {
        Pageable pageable = PageRequest.of(
                page, size, Sort.by("id")
        );
        Page<Workspace> workspaces = workspaceRepository.findAll(pageable);
        return workspaces.map(this::mapToWorkspace);
    }

    @Transactional
    public MemberResponse inviteMember(String id, @Valid InviteMemberRequest request) {
        UUID workspaceId = validationService.parseWorkspaceId(id);
        if (request.getRole() == WorkspaceRole.OWNER) {
            throw new BusinessException("Owner role cannot be assigned", ErrorCode.BAD_REQUEST);
        }
        Workspace workspace = workspaceAccessService.getWorkspaceById(workspaceId);
        WorkspaceMember currMember = workspaceAccessService.getCurrentWorkspaceMember(workspaceId);
        if (!canManageMembers(currMember.getRole(), request.getRole())) {
            throw new BusinessException("You do not have access to invite new members", ErrorCode.FORBIDDEN);
        }
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new BusinessException("User not Found", ErrorCode.NOT_FOUND));
        if (workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, user.getId())) {
            throw new BusinessException("Member already exists", ErrorCode.MEMBER_ALREADY_EXISTS);
        }
        WorkspaceMember member = new WorkspaceMember();
        member.setWorkspace(workspace);
        member.setUser(user);
        member.setRole(request.getRole());
        workspaceMemberRepository.save(member);
        return mapToMemberResponse(member);
    }


    public List<WorkspaceMemberResponse> getWorkspaceMembers(String id) {
        List<WorkspaceMember> members = workspaceAccessService.getWorkspaceMembers(id);
        return members.stream().map(this::mapToWorkspaceMember).toList();
    }



    @Transactional
    public MemberResponse updateMemberRole(String id, @Valid UpdateRoleRequest request) {
        UUID workspaceId = validationService.parseWorkspaceId(id);
        UUID currUser = currentUserService.getCurrentUserId();

        workspaceAccessService.getWorkspaceById(workspaceId);
        if (request.getRole() == WorkspaceRole.OWNER) {
            throw new BusinessException("Owner role cannot be assigned", ErrorCode.BAD_REQUEST);
        }
        WorkspaceMember tarMember = workspaceAccessService.getWorkspaceMember(workspaceId,request.getId());
        WorkspaceMember currMember = workspaceAccessService.getCurrentWorkspaceMember(workspaceId);
        if (tarMember.getUser().getId().equals(currUser)) {
            throw new BusinessException("You cannot modify your own role", ErrorCode.FORBIDDEN);
        }
        if (tarMember.getRole() == WorkspaceRole.OWNER) {
            throw new BusinessException("Workspace owner role cannot be modified", ErrorCode.FORBIDDEN);
        }
        if (!(canManageMembers(currMember.getRole(), tarMember.getRole())
                && canAssignRole(currMember.getRole(), request.getRole()))) {
            throw new BusinessException("Access Denied: Cannot assign member role", ErrorCode.FORBIDDEN);
        }
        tarMember.setRole(request.getRole());
        workspaceMemberRepository.save(tarMember);
        return mapToMemberResponse(tarMember);
    }

    @Transactional
    public void deleteMember(String id, @Valid DeleteMemberRequest request) {
        UUID workspaceId = validationService.parseWorkspaceId(id);
        UUID currUser = currentUserService.getCurrentUserId();
        workspaceAccessService.getWorkspaceById(workspaceId);
        WorkspaceMember tarMember = workspaceAccessService.getWorkspaceMember(workspaceId,request.getId());
        WorkspaceMember currMember = workspaceAccessService.getCurrentWorkspaceMember(workspaceId);
        if (tarMember.getRole() == WorkspaceRole.OWNER) {
            throw new BusinessException(
                    "Workspace owner cannot be removed",
                    ErrorCode.FORBIDDEN
            );
        }
        if (tarMember.getUser().getId().equals(currUser)) {
            throw new BusinessException(
                    "You cannot remove yourself",
                    ErrorCode.FORBIDDEN
            );
        }
        if (!canRemoveMember(currMember.getRole(), tarMember.getRole())) {
            throw new BusinessException("Access Denied: You dont have access to delete this member", ErrorCode.FORBIDDEN);
        }
        workspaceMemberRepository.delete(tarMember);
    }

    private MemberResponse mapToMemberResponse(WorkspaceMember member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getUser().getEmail())
                .workspaceName(member.getWorkspace().getName())
                .role(member.getRole())
                .build();
    }

    private UserResponse mapToUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().getRole())
                .build();
    }

    private WorkspaceResponse mapToWorkspace(Workspace workspace) {
        return WorkspaceResponse
                .builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .owner(mapToUser(workspace.getOwner()))
                .build();
    }

    private WorkspaceMemberResponse mapToWorkspaceMember(WorkspaceMember member) {
        return WorkspaceMemberResponse.builder().userId(member.getUser().getId())
                .role(member.getRole())
                .email(member.getUser().getEmail())
                .build();
    }

    private boolean canManageMembers(
            WorkspaceRole actor,
            WorkspaceRole target
    ) {
        return canManageRole(actor, target);
    }

    private boolean canAssignRole(WorkspaceRole actor, WorkspaceRole newRole) {
        return switch (actor) {
            case OWNER -> newRole == WorkspaceRole.MAINTAINER
                    || newRole == WorkspaceRole.MEMBER
                    || newRole == WorkspaceRole.VIEWER;

            case MAINTAINER -> newRole == WorkspaceRole.MEMBER
                    || newRole == WorkspaceRole.VIEWER;

            default -> false;
        };
    }

    private boolean canRemoveMember(
            WorkspaceRole actor,
            WorkspaceRole target
    ) {
        return canManageRole(actor, target);
    }

    private boolean canManageRole(WorkspaceRole actor, WorkspaceRole target) {
        return switch (actor) {
            case OWNER -> target == WorkspaceRole.MAINTAINER
                    || target == WorkspaceRole.MEMBER
                    || target == WorkspaceRole.VIEWER;

            case MAINTAINER -> target == WorkspaceRole.MEMBER
                    || target == WorkspaceRole.VIEWER;

            case MEMBER, VIEWER -> false;
        };
    }


}
