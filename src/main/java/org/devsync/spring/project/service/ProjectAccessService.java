    package org.devsync.spring.project.service;

    import lombok.RequiredArgsConstructor;
    import org.devsync.spring.common.exception.BusinessException;
    import org.devsync.spring.common.exception.ErrorCode;
    import org.devsync.spring.common.security.CurrentUserService;
    import org.devsync.spring.project.entity.Project;
    import org.devsync.spring.project.repository.ProjectRepository;
    import org.devsync.spring.workspace.entity.Workspace;
    import org.devsync.spring.workspace.entity.WorkspaceMember;
    import org.devsync.spring.workspace.repository.WorkspaceMemberRepository;
    import org.springframework.stereotype.Service;

    import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class ProjectAccessService {

        private final ProjectRepository projectRepository;
        private final CurrentUserService currentUserService;
        private final WorkspaceMemberRepository workspaceMemberRepository;

        public Project getProjectById(UUID projectUUID) {
            return projectRepository.findById(projectUUID).orElseThrow(
                    () -> new BusinessException("Project not found", ErrorCode.NOT_FOUND)
            );
        }

        public WorkspaceMember getCurrentProjectMember(Project project) {
            UUID currUser = currentUserService.getCurrentUserId();
            Workspace workspace = project.getWorkspace();
            return workspaceMemberRepository.findByWorkspaceIdAndUserId(workspace.getId(), currUser).orElseThrow(
                    () -> new BusinessException("Access Denied: You do not have access to this workspace", ErrorCode.FORBIDDEN)
            );
        }
    }
