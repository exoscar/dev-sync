package org.devsync.spring.issue.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.util.Utils;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.entity.IssuePriority;
import org.devsync.spring.issue.entity.IssueStatus;
import org.devsync.spring.project.service.ProjectValidationService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueValidationService {
    private final ProjectValidationService projectValidationService;

    public UUID parseIssueId(String id) {
        return Utils.parseUuid(id, "Invalid Issue Id");
    }

    public void validateStatusChange(Issue issue, IssueStatus status) {
        if (issue.getStatus() == status) {
            throw new BusinessException("Status can not be same", ErrorCode.BAD_REQUEST);
        }
    }

    public void validatePriorityChange(Issue issue, IssuePriority priority) {
        if (issue.getPriority() == priority) {
            throw new BusinessException(
                    "Priority cannot be same",
                    ErrorCode.BAD_REQUEST
            );
        }
    }

    public void validateAssignee(Issue issue, UUID assigneeId) {
        if (issue.getAssignee() != null && issue.getAssignee().getId().equals(assigneeId)) {
            throw new BusinessException(
                    "Issue already assigned to this user",
                    ErrorCode.BAD_REQUEST
            );
        }
    }
}
