package org.devsync.spring.project.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.common.util.Utils;
import org.devsync.spring.project.dto.CreateProjectRequest;
import org.devsync.spring.project.dto.ProjectResponse;
import org.devsync.spring.project.dto.UpdateProjectRequest;
import org.devsync.spring.project.entity.Project;
import org.devsync.spring.project.repository.ProjectRepository;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.entity.WorkspaceRole;
import org.devsync.spring.workspace.repository.WorkspaceMemberRepository;
import org.devsync.spring.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final CurrentUserService currentUserService;
    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Transactional
    public ProjectResponse createProject(String id, @Valid CreateProjectRequest request) {
        UUID workspaceId = parseWorkspaceId(id);
        UUID currUser = currentUserService.getCurrentUserId();
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(
                () -> new BusinessException("Workspace not found", ErrorCode.NOT_FOUND)
        );


        WorkspaceMember member = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId,currUser)
                .orElseThrow(()-> new BusinessException("Access Denied: You do not have access to workspace",ErrorCode.FORBIDDEN));
        if(!canManageProject(member.getRole())){
            throw new BusinessException("Access Denied: You do not have access to create project",ErrorCode.FORBIDDEN);
        }
        if(projectRepository.existsByWorkspaceIdAndNameIgnoreCase(workspaceId,request.getName())){
            throw new BusinessException("Project already exists in the current workspace",ErrorCode.PROJECT_ALREADY_EXISTS);
        }

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setWorkspace(workspace);
        projectRepository.save(project);

        return maptoProjectResponse(project);

    }


    public List<ProjectResponse> getAllProjects(String id) {
        UUID workspaceId = parseWorkspaceId(id);
        UUID currUser = currentUserService.getCurrentUserId();
        if(!workspaceRepository.existsById(workspaceId)){
            throw new BusinessException("Workspace not found",ErrorCode.NOT_FOUND);
        }
        if(!workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId,currUser)){
            throw new BusinessException("Access Denied: You do not have access to workspace",ErrorCode.FORBIDDEN);
        }
        List<Project> projects = projectRepository.findByWorkspaceId(workspaceId);
        return projects.stream().map(this::maptoProjectResponse).toList();
    }

    public ProjectResponse getProjectById(String workspaceId, String id) {
        UUID workspaceUUID = parseWorkspaceId(workspaceId);
        UUID projectId = parseProjectId(id);
        UUID currUser = currentUserService.getCurrentUserId();
        if(!workspaceRepository.existsById(workspaceUUID)){
            throw new BusinessException("Workspace not found",ErrorCode.NOT_FOUND);
        }
        if(!workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceUUID,currUser)){
            throw new BusinessException("Access Denied: You do not have access to workspace",ErrorCode.FORBIDDEN);
        }
        Project project = projectRepository.findByWorkspaceIdAndId(workspaceUUID,projectId).orElseThrow(
                ()-> new BusinessException("Project not found",ErrorCode.NOT_FOUND)
        );
        return maptoProjectResponse(project);
    }

    @Transactional
    public ProjectResponse updateProject(String workspaceId, String id, UpdateProjectRequest request) {
        UUID workspaceUUID = parseWorkspaceId(workspaceId);
        UUID projectId = parseProjectId(id);
        UUID currUser = currentUserService.getCurrentUserId();
        if(!workspaceRepository.existsById(workspaceUUID)){
            throw new BusinessException("Workspace not found",ErrorCode.NOT_FOUND);
        }
        WorkspaceMember member = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceUUID,currUser)
                .orElseThrow(()-> new BusinessException("Access Denied: You do not have access to workspace",ErrorCode.FORBIDDEN));

        Project project = projectRepository.findByWorkspaceIdAndId(workspaceUUID,projectId).orElseThrow(
                ()-> new BusinessException("Project not found",ErrorCode.NOT_FOUND)
        );
        if(!canManageProject(member.getRole())){
            throw new BusinessException("Access Denied: You do not have access to update project",ErrorCode.FORBIDDEN);
        }
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        projectRepository.save(project);

        return maptoProjectResponse(project);
    }

    @Transactional
    public void deleteProject(String workspaceId, String id) {
        UUID workspaceUUID = parseWorkspaceId(workspaceId);
        UUID projectId = parseProjectId(id);
        UUID currUser = currentUserService.getCurrentUserId();
        if(!workspaceRepository.existsById(workspaceUUID)){
            throw new BusinessException("Workspace not found",ErrorCode.NOT_FOUND);
        }
        WorkspaceMember member = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceUUID,currUser)
                .orElseThrow(()-> new BusinessException("Access Denied: You do not have access to workspace",ErrorCode.FORBIDDEN));

        Project project = projectRepository.findByWorkspaceIdAndId(workspaceUUID,projectId).orElseThrow(
                ()-> new BusinessException("Project not found",ErrorCode.NOT_FOUND)
        );
        if(!canManageProject(member.getRole())){
            throw new BusinessException("Access Denied: You do not have access to delete project",ErrorCode.FORBIDDEN);
        }
        projectRepository.delete(project);
    }



    private ProjectResponse maptoProjectResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .workspaceId(project.getWorkspace().getId())
                .workspaceName(project.getWorkspace().getName())
                .build();
    }


    private boolean canManageProject(WorkspaceRole role){
       return role == WorkspaceRole.OWNER
               || role == WorkspaceRole.MAINTAINER;
    }
    private UUID parseWorkspaceId(String id){
        return  Utils.parseUuid(id,"Invalid Workspace Id");
    }

    private UUID parseProjectId(String id){
        return Utils.parseUuid(id,"Invalid Project Id");
    }



}
