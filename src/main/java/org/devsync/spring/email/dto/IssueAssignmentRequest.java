package org.devsync.spring.email.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.auth.entity.User;

import java.util.List;

@Builder
@Data
public class IssueAssignmentRequest {
    private String title;
    private String description;
    private List<EmailRecipient> emailRecipients;
    private String projectName;
    private String workspaceName;
}
