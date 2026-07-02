package org.devsync.spring.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devsync.spring.email.dto.EmailRecipient;
import org.devsync.spring.email.dto.IssueAssignmentRequest;
import org.devsync.spring.email.dto.IssuePriorityChange;
import org.devsync.spring.email.dto.IssueStatusChange;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationImpl implements EmailNotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendIssueAssignedEmail(IssueAssignmentRequest request) {
        sendHtmlEmail(
                request.getEmailRecipients()
                        .stream()
                        .map(EmailRecipient::getEmail)
                        .toArray(String[]::new),
                "Issue Assigned",
                buildIssueAssignedHtml(request)
        );
    }

    @Override
    public void sendIssueStatusChangedEmail(IssueStatusChange request) {
        sendHtmlEmail(
                request.getEmailRecipients()
                        .stream()
                        .map(EmailRecipient::getEmail)
                        .toArray(String[]::new),
                "Issue Status Changed",
                buildStatusChangedHtml(request)
        );
    }

    @Override
    public void sendIssuePriorityChangedEmail(IssuePriorityChange request) {
        sendHtmlEmail(
                request.getEmailRecipients()
                        .stream()
                        .map(EmailRecipient::getEmail)
                        .toArray(String[]::new),
                "Issue Priority Changed",
                buildPriorityChangedHtml(request)
        );
    }

    private void sendHtmlEmail(
            String[] recipients,
            String subject,
            String htmlContent
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setBcc(recipients);
            helper.setFrom("noreply@devsync.com");
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email sent successfully. Subject={}", subject);
        } catch (MailException | MessagingException ex) {
            log.error("Failed to send email. Subject={}", subject, ex);
        }
    }

    private String buildIssueAssignedHtml(
            IssueAssignmentRequest request
    ) {
        return """
                 <h2>Issue Assigned</h2>
                
                <p>You have been assigned a new issue.</p>
                <b>Title:</b> %s
                <br><br>
                <b>Description:</b> %s
                <br><br>
                <b>Project:</b> %s
                <br>
                <b>Workspace:</b> %s
                """
                .formatted(
                        request.getTitle(),
                        request.getDescription(),
                        request.getProjectName(),
                        request.getWorkspaceName()
                );
    }

    private String buildStatusChangedHtml(
            IssueStatusChange request
    ) {
        return """
                 <h2>Issue Status Changed</h2>
                <b>Title:</b> %s
                <br><br>
                <b>Description:</b> %s
                <br><br>
                <p>
                    Issue status changed from
                    <b>%s</b>
                    to
                    <b>%s</b>
                </p>
                <b>Project:</b> %s
                <br>
                <b>Workspace:</b> %s
                """
                .formatted(
                        request.getTitle(),
                        request.getDescription(),
                        request.getOldStatus(),
                        request.getNewStatus(),
                        request.getProjectName(),
                        request.getWorkspaceName()
                );

    }

    private String buildPriorityChangedHtml(
            IssuePriorityChange request
    ) {
        return """
                 <h2>Issue Priority Changed</h2>
                <b>Title:</b> %s
                <br><br>
                <b>Description:</b> %s
                <br><br>
                <p>
                    Issue priority changed from
                    <b>%s</b>
                    to
                    <b>%s</b>
                </p>
                <b>Project:</b> %s
                <br>
                <b>Workspace:</b> %s
                """
                .formatted(
                        request.getTitle(),
                        request.getDescription(),
                        request.getOldPriority(),
                        request.getNewPriority(),
                        request.getProjectName(),
                        request.getWorkspaceName()
                );
    }
}
