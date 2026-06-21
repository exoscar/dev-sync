package org.devsync.spring.label.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.util.Utils;
import org.devsync.spring.label.entity.Label;
import org.devsync.spring.label.entity.LabelStatus;
import org.devsync.spring.label.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LabelValidationService {
    private final LabelRepository labelRepository;

    public UUID parseLabelId(String id) {
        return Utils.parseUuid(id, "Invalid label id");
    }

    public String normalizeName(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public void validateLabelNameUnique(UUID workspaceUUID, String normalizedName) {
        if (labelRepository.existsByWorkspaceIdAndNormalizedName(workspaceUUID, normalizedName)) {
            throw new BusinessException("Label already exists", ErrorCode.LABEL_ALREADY_EXISTS);
        }
    }

    public void validateLabelNameUniqueForUpdate(UUID workspaceId,
                                                 String normalizedName,
                                                 UUID labelId
    ) {
        if (labelRepository.existsByWorkspaceIdAndNormalizedNameAndIdNot(workspaceId, normalizedName, labelId)) {
            throw new BusinessException("Label already exists", ErrorCode.LABEL_ALREADY_EXISTS);
        }
    }

    public void validateLabelIsActive(Label label) {
        if (label.getStatus() != LabelStatus.ACTIVE) {
            throw new BusinessException("Access denied: Label is not active", ErrorCode.FORBIDDEN);
        }
    }



}
