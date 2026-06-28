package org.devsync.spring.workspace.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.issue.dto.IssueResponse;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.mapper.IssueMapper;
import org.devsync.spring.issue.repository.IssueRepository;
import org.devsync.spring.issue.specification.IssueSpecification;
import org.devsync.spring.workspace.dto.WorkspaceIssueFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceIssueService {

    private final WorkspaceAccessService workspaceAccessService;
    private final IssueRepository issueRepository;
    private final IssueMapper mapper;

    public Page<IssueResponse> filter(UUID workspaceId, WorkspaceIssueFilterRequest request, int page, int size) {
        workspaceAccessService.getWorkspaceWithMembershipCheck(workspaceId);
        Specification<Issue> spec = IssueSpecification.hasWorkspace(workspaceId);
        validate(request);
        if (request.getStatus() != null) {
            spec = spec.and(IssueSpecification.hasStatus(request.getStatus()));
        }
        if (request.getPriority() != null) {
            spec = spec.and(IssueSpecification.hasPriority(request.getPriority()));
        }
        if (request.getAssigneeId() != null) {
            spec = spec.and(IssueSpecification.hasAssignee(request.getAssigneeId()));
        }
        if (request.getProjectId() != null) {
            spec = spec.and(IssueSpecification.hasProject(request.getProjectId()));
        }
        if (request.getUnassigned() != null && request.getUnassigned()) {
            spec = spec.and(IssueSpecification.isUnassigned());
        }
        if (request.getCreatedFrom() != null) {
            spec = spec.and(IssueSpecification.createdAfter(request.getCreatedFrom()));
        }
        if (request.getCreatedTo() != null) {
            spec = spec.and(IssueSpecification.createdBefore(request.getCreatedTo()));
        }
        if (request.getUpdatedFrom() != null) {
            spec = spec.and(IssueSpecification.updatedAfter(request.getUpdatedFrom()));
        }
        if (request.getUpdatedTo() != null) {
            spec = spec.and(IssueSpecification.updatedBefore(request.getUpdatedTo()));
        }
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Issue> issues = issueRepository.findAll(spec, pageable);
        return issues.map(mapper::toResponse);
    }

    private void validate(WorkspaceIssueFilterRequest request) {
        validateAssigneeFilters(request);
        validateDateRange(request.getCreatedFrom(), request.getCreatedTo(), "Created");
        validateDateRange(request.getUpdatedFrom(), request.getUpdatedTo(), "Updated");
    }

    private void validateAssigneeFilters(WorkspaceIssueFilterRequest request) {
        if (request.getAssigneeId() != null
                && Boolean.TRUE.equals(request.getUnassigned())) {
            throw new BusinessException(
                    "Cannot filter by assigneeId and unassigned together",
                    ErrorCode.BAD_REQUEST
            );
        }
    }

    private void validateDateRange(
            Instant from,
            Instant to,
            String fieldName
    ) {
        Instant now = Instant.now();
        if (from != null && from.isAfter(now)) {
            throw new BusinessException(
                    fieldName + " from date cannot be in the future",
                    ErrorCode.BAD_REQUEST
            );
        }
        if (to != null && to.isAfter(now)) {
            throw new BusinessException(
                    fieldName + " to date cannot be in the future",
                    ErrorCode.BAD_REQUEST
            );
        }
        if (from != null && to != null && from.isAfter(to)) {
            throw new BusinessException(
                    fieldName + " from date cannot be after " + fieldName.toLowerCase() + " to date",
                    ErrorCode.BAD_REQUEST
            );
        }
    }
}
