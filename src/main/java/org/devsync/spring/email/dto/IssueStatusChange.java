package org.devsync.spring.email.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.issue.entity.IssueStatus;

import java.util.List;

@Builder
@Data
public class IssueStatusChange {
    private String title;
    private String description;
    private List<EmailRecipient> emailRecipients;
    private String projectName;
    private String workspaceName;
    private IssueStatus oldStatus;
    private IssueStatus newStatus;
}
