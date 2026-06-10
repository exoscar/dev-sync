package org.devsync.spring.workspace.service;


import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.auth.repository.UserRepository;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.common.util.Utils;
import org.devsync.spring.user.dto.UserResponse;
import org.devsync.spring.workspace.dto.CreateWorkspaceRequest;
import org.devsync.spring.workspace.dto.WorkspaceResponse;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.repository.WorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    public WorkspaceResponse createWorkspace(CreateWorkspaceRequest request) {
        UUID currUser = currentUserService.getCurrentUserId();

        User user = userRepository.findById(currUser).orElseThrow(
                () -> new BusinessException("User not Found", ErrorCode.NOT_FOUND));

        if(workspaceRepository.existsByNameIgnoreCase(request.getName())){
            throw new BusinessException("Workspace name already exist",ErrorCode.WORKSPACE_NAME_ALREADY_EXISTS);
        }

        Workspace workspace = new Workspace();
        workspace.setName(request.getName());
        workspace.setOwner(user);
        workspaceRepository.save(workspace);

        return mapToWorkspace(workspace);
    }

    public WorkspaceResponse getWorkspaceById(String id) {
        UUID workspaceId = Utils.parseUuid(id,"Invalid Workspace Id");
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(
                ()->new BusinessException("Workspace not found",ErrorCode.NOT_FOUND)
        );

        return mapToWorkspace(workspace);
    }

    public List<WorkspaceResponse> getUserWorkspaces() {
        UUID currUser = currentUserService.getCurrentUserId();
        List<Workspace> workspaces = workspaceRepository.findAllByOwnerId(currUser);
        return workspaces.stream().map(this::mapToWorkspace).toList();
    }

    public Page<WorkspaceResponse> getAllWorkspaces(int page,int size) {
        Pageable pageable = PageRequest.of(
                page,size, Sort.by("id")
        );
        Page<Workspace> workspaces = workspaceRepository.findAll(pageable);
        return workspaces.map(this::mapToWorkspace);
    }


    private UserResponse mapToUser(User user){
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().getRole())
                .build();
    }

    private WorkspaceResponse mapToWorkspace(Workspace workspace){
       return WorkspaceResponse
                .builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .owner(mapToUser(workspace.getOwner()))
                .build();
    }


}
