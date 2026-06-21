package org.devsync.spring.label.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.label.dto.CreateLabelRequest;
import org.devsync.spring.label.dto.LabelResponse;
import org.devsync.spring.label.dto.UpdateLabelRequest;
import org.devsync.spring.label.entity.Label;
import org.devsync.spring.label.entity.LabelStatus;
import org.devsync.spring.label.mapper.LabelMapper;
import org.devsync.spring.label.repository.LabelRepository;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.service.WorkspaceAccessService;
import org.devsync.spring.workspace.service.WorkspaceValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;
    private final WorkspaceAccessService workspaceAccessService;
    private final WorkspaceValidationService workspaceValidationService;
    private final LabelAccessService labelAccessService;
    private final LabelValidationService validationService;

    @Transactional
    public LabelResponse createLabel(String workspaceId, @Valid CreateLabelRequest request) {
        UUID workspaceUUID = workspaceValidationService.parseWorkspaceId(workspaceId);
        WorkspaceMember member = labelAccessService.validateManageLabelsPermission(workspaceUUID);
        String name = request.getName().trim();
        String normalizedName = validationService.normalizeName(name);
        validationService.validateLabelNameUnique(workspaceUUID, normalizedName);
        Label label = new Label();
        label.setName(name);
        label.setNormalizedName(normalizedName);
        label.setColor(request.getColor().toUpperCase(Locale.ROOT));
        label.setDescription(request.getDescription());
        label.setStatus(LabelStatus.ACTIVE);
        label.setWorkspace(member.getWorkspace());
        labelRepository.save(label);
        return labelMapper.toResponse(label);
    }

    @Transactional(readOnly = true)
    public List<LabelResponse> getLabels(String workspaceId) {
        UUID workspaceUUID = workspaceValidationService.parseWorkspaceId(workspaceId);
        workspaceAccessService.getWorkspaceWithMembershipCheck(workspaceUUID);
        List<Label> labels = labelRepository.findAllByWorkspaceIdAndStatus(workspaceUUID, LabelStatus.ACTIVE);
        return labels.stream().map(labelMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public LabelResponse getLabelByWorkspaceIdAndId(String workspaceId, String labelId) {
        Label label = labelAccessService.getLabel(workspaceId, labelId);
        return labelMapper.toResponse(label);
    }

    @Transactional
    public LabelResponse updateLabel(String workspaceId, String labelId, @Valid UpdateLabelRequest request) {
        Label label = labelAccessService.getLabel(workspaceId, labelId);
        validationService.validateLabelIsActive(label);
        UUID workspaceUUID = label.getWorkspace().getId();
        String name = request.getName().trim();
        labelAccessService.validateManageLabelsPermission(workspaceUUID);
        String normalizedName = validationService.normalizeName(name);
        validationService.validateLabelNameUniqueForUpdate(workspaceUUID, normalizedName,label.getId());

        label.setNormalizedName(normalizedName);
        label.setColor(request.getColor().toUpperCase(Locale.ROOT));
        label.setDescription(request.getDescription());
        label.setName(name);
        return labelMapper.toResponse(label);
    }

    @Transactional
    public void archiveLabel(String workspaceId, String labelId) {
        Label label = labelAccessService.getLabel(workspaceId, labelId);
        UUID workspaceUUID = label.getWorkspace().getId();
        labelAccessService.validateManageLabelsPermission(workspaceUUID);
        label.setStatus(LabelStatus.ARCHIVED);

    }
}
