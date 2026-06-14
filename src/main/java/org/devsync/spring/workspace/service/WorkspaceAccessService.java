package org.devsync.spring.workspace.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.repository.WorkspaceMemberRepository;
import org.devsync.spring.workspace.repository.WorkspaceRepository;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WorkspaceAccessService {
    private final WorkspaceValidationService  validationService;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final CurrentUserService currentUserService;

    public Workspace getWorkspaceWithMembershipCheck(String workspaceId){
     return getWorkspaceWithMembershipCheck(validationService.parseWorkspaceId(workspaceId));
    }
    public Workspace getWorkspaceWithMembershipCheck(UUID workspaceId){
        Workspace workspace = getWorkspaceById(workspaceId);
        UUID currentUser = currentUserService.getCurrentUserId();
        if (!workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, currentUser)) {
            throw new BusinessException("You do not have access to this workspace", ErrorCode.FORBIDDEN);
        }
        return workspace;
    }

    public Workspace getWorkspaceById(UUID workspaceId){
        return workspaceRepository.findById(workspaceId).orElseThrow(
                ()-> new BusinessException("Workspace not found", ErrorCode.NOT_FOUND)
        );
    }

    public WorkspaceMember getWorkspaceMember(UUID workspaceId,UUID userId){
        return workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, userId)
                .orElseThrow(() ->
                        {
                            if (userId.equals(currentUserService.getCurrentUserId())) {
                                return new BusinessException("You do not have access to this workspace", ErrorCode.FORBIDDEN);
                            } else {
                               return new BusinessException("Member not found", ErrorCode.NOT_FOUND);
                            }
                        }
                        );
    }

    public WorkspaceMember getCurrentWorkspaceMember(String workspaceId){
        UUID workspaceUUID = validationService.parseWorkspaceId(workspaceId);
       return getCurrentWorkspaceMember(workspaceUUID);
    }

    public WorkspaceMember getCurrentWorkspaceMember(UUID workspaceId){
        UUID currentUser = currentUserService.getCurrentUserId();
        return getWorkspaceMember(workspaceId,currentUser);
    }

    public List<WorkspaceMember> getWorkspaceMembers(UUID workspaceId){
        Workspace workspace = getWorkspaceWithMembershipCheck(workspaceId);
        return workspaceMemberRepository.findByWorkspaceId(workspace.getId());
    }
    public List<WorkspaceMember> getWorkspaceMembers(String workspaceId){
        Workspace workspace = getWorkspaceWithMembershipCheck(workspaceId);
        return workspaceMemberRepository.findByWorkspaceId(workspace.getId());
    }
}
