package org.devsync.spring.issue.factory;

import org.devsync.spring.issue.dto.CreateIssueRequest;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.entity.IssueStatus;
import org.devsync.spring.project.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class IssueFactory {
    public Issue create(
            Project project,
            CreateIssueRequest request
    ) {
        Issue issue = new Issue();
        issue.setTitle(request.getTitle().trim());
        issue.setDescription(request.getDescription().trim());
        issue.setPriority(request.getPriority());
        issue.setStatus(IssueStatus.TODO);
        issue.setProject(project);
        return issue;
    }
}
