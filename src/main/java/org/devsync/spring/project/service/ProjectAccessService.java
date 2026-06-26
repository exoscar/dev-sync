package org.devsync.spring.project.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.project.entity.Project;
import org.devsync.spring.project.repository.ProjectRepository;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.service.WorkspaceAccessService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectAccessService {

    private final ProjectRepository projectRepository;
    private final WorkspaceAccessService workspaceAccessService;
    private final ProjectValidationService projectValidationService;

    public Project getProjectById(UUID projectUUID) {
        return projectRepository.findById(projectUUID).orElseThrow(
                () -> new BusinessException("Project not found", ErrorCode.NOT_FOUND)
        );
    }

    public WorkspaceMember getCurrentProjectMember(Project project) {
        Workspace workspace = project.getWorkspace();
        return workspaceAccessService.getCurrentWorkspaceMember(workspace.getId());
    }

    public Project getProjectWithMembershipCheck(UUID projectUUID) {
        Project project = projectRepository.findById(projectUUID).orElseThrow(
                () -> new BusinessException("Project not found", ErrorCode.NOT_FOUND)
        );
        getCurrentProjectMember(project);
        return project;
    }

    public Project getProjectWithMembershipCheck(String projectId){
        UUID projectUUID = projectValidationService.parseProjectId(projectId);
        return getProjectWithMembershipCheck(projectUUID);
    }

}
