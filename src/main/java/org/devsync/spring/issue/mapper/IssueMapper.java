package org.devsync.spring.issue.mapper;


import org.devsync.spring.auth.entity.User;
import org.devsync.spring.issue.dto.IssueResponse;
import org.devsync.spring.issue.entity.Issue;
import org.springframework.stereotype.Component;

@Component
public class IssueMapper {
    public IssueResponse toResponse(Issue issue) {
        User assignee = issue.getAssignee();
        return IssueResponse.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .projectId(issue.getProject().getId())
                .projectName(issue.getProject().getName())
                .priority(issue.getPriority())
                .assigneeId(
                        assignee != null ? assignee.getId() : null
                )
                .assigneeEmail(
                        assignee != null ? assignee.getEmail() : null
                )
                .build();
    }
}
