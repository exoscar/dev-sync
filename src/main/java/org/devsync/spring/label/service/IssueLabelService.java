package org.devsync.spring.label.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.service.IssueAccessService;
import org.devsync.spring.issue.service.IssueValidationService;
import org.devsync.spring.label.dto.AddIssueLabelRequest;
import org.devsync.spring.label.dto.IssueLabelResponse;
import org.devsync.spring.label.entity.IssueLabel;
import org.devsync.spring.label.entity.Label;
import org.devsync.spring.label.mapper.IssueLabelMapper;
import org.devsync.spring.label.repository.IssueLabelRepository;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.service.WorkspaceAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueLabelService {
    private final IssueAccessService issueAccessService;
    private final LabelAccessService labelAccessService;
    private final IssueValidationService issueValidationService;
    private final LabelValidationService labelValidationService;
    private final IssueLabelValidationService issueLabelValidationService;
    private final IssueLabelMapper mapper;
    private final WorkspaceAccessService workspaceAccessService;
    private final IssueLabelRepository issueLabelRepository;

    @Transactional
    public IssueLabelResponse addLabel(String issueId, @Valid AddIssueLabelRequest request) {
        UUID issueUUID = issueValidationService.parseIssueId(issueId);
        UUID labelUUID = labelValidationService.parseLabelId(request.getLabelId());
        Issue issue = issueAccessService.getIssue(issueUUID);
        WorkspaceMember member = workspaceAccessService.getCurrentWorkspaceMember(issue.getProject().getWorkspace().getId());

        Label label = labelAccessService.getLabel(member.getWorkspace().getId(),labelUUID);
        issueLabelValidationService.validateManageIssueLabelsPermission(member);
        issueLabelValidationService.validateLabelAssignable(label);
        issueLabelValidationService.validateLabelNotAlreadyAssigned(issue,label);
        IssueLabel issueLabel = new IssueLabel();
        issueLabel.setIssue(issue);
        issueLabel.setLabel(label);
        issueLabel.setCreatedBy(member.getUser());
        issueLabelRepository.save(issueLabel);

        return mapper.toResponse(issueLabel);

    }

    @Transactional
    public void removeLabel(String issueId, String labelId) {
        UUID issueUUID = issueValidationService.parseIssueId(issueId);
        UUID labelUUID = labelValidationService.parseLabelId(labelId);
        Issue issue = issueAccessService.getIssue(issueUUID);
        WorkspaceMember member = workspaceAccessService.getCurrentWorkspaceMember(issue.getProject().getWorkspace().getId());
        labelAccessService.getLabel(member.getWorkspace().getId(),labelUUID);
        issueLabelValidationService.validateManageIssueLabelsPermission(member);
        issueLabelRepository.deleteByIssueIdAndLabelId(issueUUID,labelUUID);
    }

    @Transactional(readOnly = true)
    public List<IssueLabelResponse> getLabels(String issueId) {
        UUID issueUUID = issueValidationService.parseIssueId(issueId);
        Issue issue = issueAccessService.getIssue(issueUUID);
        workspaceAccessService.getWorkspaceWithMembershipCheck(issue.getProject().getWorkspace().getId());
        List<IssueLabel> issueLabels = issueLabelRepository.findByIssueId(issueUUID);
        return issueLabels.stream().map(mapper::toResponse).toList();
    }
}
