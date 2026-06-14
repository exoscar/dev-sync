package org.devsync.spring.issue.context;

import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.project.entity.Project;
import org.devsync.spring.workspace.entity.WorkspaceMember;

public record IssueContext(Project project,
                           Issue issue,
                           WorkspaceMember member) {


}
