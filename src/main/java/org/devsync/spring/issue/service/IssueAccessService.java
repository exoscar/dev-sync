package org.devsync.spring.issue.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.issue.context.IssueContext;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.repository.IssueRepository;
import org.devsync.spring.project.entity.Project;
import org.devsync.spring.project.service.ProjectAccessService;
import org.devsync.spring.project.service.ProjectValidationService;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueAccessService {

    private final IssueRepository issueRepository;
    private final ProjectAccessService projectAccessService;
    private final ProjectValidationService projectValidationService;
    private final IssueValidationService issueValidationService;

    public IssueContext loadProjectContext(
            String projectId
    ) {
        UUID projectUUID = projectValidationService.parseProjectId(projectId);
        Project project = projectAccessService.getProjectById(projectUUID);
        WorkspaceMember member =
                projectAccessService.getCurrentProjectMember(project);
        return new IssueContext(
                project,
                null,
                member
        );
    }

    public IssueContext loadIssueContext(
            String projectId,
            String issueId
    ) {
        UUID projectUUID = projectValidationService.parseProjectId(projectId);
        UUID issueUUID = issueValidationService.parseIssueId(issueId);
        Project project = projectAccessService.getProjectById(projectUUID);
        Issue issue =
                getIssue(projectUUID, issueUUID);
        WorkspaceMember member =
                projectAccessService.getCurrentProjectMember(project);
        return new IssueContext(
                project,
                issue,
                member
        );
    }

    private Issue getIssue(
            UUID projectId,
            UUID issueId
    ) {
        return issueRepository
                .findByProjectIdAndId(
                        projectId,
                        issueId
                )
                .orElseThrow(() ->
                        new BusinessException(
                                "Issue not found",
                                ErrorCode.NOT_FOUND
                        ));
    }
}
