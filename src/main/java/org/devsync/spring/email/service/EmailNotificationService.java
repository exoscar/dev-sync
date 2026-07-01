package org.devsync.spring.email.service;


import org.devsync.spring.email.dto.IssueAssignmentRequest;
import org.devsync.spring.email.dto.IssuePriorityChange;
import org.devsync.spring.email.dto.IssueStatusChange;

public interface EmailNotificationService {
    void sendIssueAssignedEmail(IssueAssignmentRequest request);

    void sendIssueStatusChangedEmail(IssueStatusChange request);

    void sendIssuePriorityChangedEmail(IssuePriorityChange request);
}
