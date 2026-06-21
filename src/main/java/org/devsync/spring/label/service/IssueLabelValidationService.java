package org.devsync.spring.label.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.label.entity.Label;
import org.devsync.spring.label.entity.LabelStatus;
import org.devsync.spring.label.repository.IssueLabelRepository;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.entity.WorkspaceRole;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueLabelValidationService {

    private final IssueLabelRepository repository;

    public void validateLabelAssignable(Label label){
        if(label.getStatus()!= LabelStatus.ACTIVE) {
            throw new BusinessException("Access denied: Label is archived",ErrorCode.LABEL_ARCHIVED);
        }
    }


    public void validateLabelNotAlreadyAssigned(Issue issue, Label label) {
        if(repository.existsByIssueIdAndLabelId(issue.getId(),label.getId())){
            throw new BusinessException("Label already Assigned", ErrorCode.LABEL_ALREADY_ASSIGNED);
        }
    }

    public void validateManageIssueLabelsPermission(WorkspaceMember member) {
        if(member.getRole() == WorkspaceRole.VIEWER){
            throw new BusinessException("Access Denied: you can not perform this action",ErrorCode.FORBIDDEN);
        }
    }
}
