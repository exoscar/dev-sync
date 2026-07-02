package org.devsync.spring.email.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.issue.entity.IssuePriority;

import java.util.List;

@Builder
@Data
public class IssuePriorityChange {
    private String title;
    private String description;
    private List<EmailRecipient> emailRecipients;
    private String projectName;
    private String workspaceName;
    private IssuePriority oldPriority;
    private IssuePriority newPriority;
}

