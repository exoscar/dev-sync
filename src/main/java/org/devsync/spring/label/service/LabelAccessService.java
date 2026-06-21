package org.devsync.spring.label.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.label.entity.Label;
import org.devsync.spring.label.repository.LabelRepository;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.entity.WorkspaceRole;
import org.devsync.spring.workspace.service.WorkspaceAccessService;
import org.devsync.spring.workspace.service.WorkspaceValidationService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LabelAccessService {
    private final LabelRepository labelRepository;
    private final WorkspaceAccessService workspaceAccessService;
    private final LabelValidationService validationService;
    private final WorkspaceValidationService workspaceValidationService;

    public Label getLabel(String workspaceId, String labelId) {
        UUID labelUUID = validationService.parseLabelId(labelId);
        UUID workspaceUUID = workspaceValidationService.parseWorkspaceId(workspaceId);
      return getLabel(workspaceUUID,labelUUID);
    }

    public Label getLabel(UUID workspaceId, UUID labelId) {

        Workspace workspace = workspaceAccessService.getWorkspaceWithMembershipCheck(workspaceId);
        return labelRepository.findByWorkspaceIdAndId(workspace.getId(),labelId).orElseThrow(
                ()-> new BusinessException("Label not found", ErrorCode.NOT_FOUND)
        );
    }

    public WorkspaceMember validateManageLabelsPermission(UUID workspaceId){
        WorkspaceMember member = workspaceAccessService.getCurrentWorkspaceMember(workspaceId);
        if (!(member.getRole() == WorkspaceRole.MAINTAINER || member.getRole() == WorkspaceRole.OWNER)) {
            throw new BusinessException("Access denied:You can not perform this action", ErrorCode.FORBIDDEN);
        }
        return member;
    }

}
