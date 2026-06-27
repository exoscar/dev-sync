package org.devsync.spring.notification.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.notification.dto.NotificationResponse;
import org.devsync.spring.notification.dto.UnreadCountResponse;
import org.devsync.spring.notification.entity.Notification;
import org.devsync.spring.notification.mapper.NotificationMapper;
import org.devsync.spring.notification.repository.NotificationRepository;
import org.devsync.spring.project.entity.Project;
import org.devsync.spring.project.service.ProjectAccessService;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.service.WorkspaceAccessService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;
    private final WorkspaceAccessService workspaceAccessService;
    private final ProjectAccessService projectAccessService;
    private final CurrentUserService currentUserService;
    private final NotificationMapper mapper;
    private final NotificationValidationService validationService;
    private final NotificationAccessService accessService;

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getMyWorkspaceNotifications(String workspaceId, int page, int size) {
        Workspace workspace = workspaceAccessService.getWorkspaceWithMembershipCheck(workspaceId);
        UUID currUserId = currentUserService.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = repository.findByWorkspaceIdAndRecipientIdOrderByCreatedAtDesc(workspace.getId(), currUserId, pageable);
        return notifications.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getMyProjectNotifications(String projectId, int page, int size) {
        Project project = projectAccessService.getProjectWithMembershipCheck(projectId);
        UUID currUserId = currentUserService.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = repository.findByProjectIdAndRecipientIdOrderByCreatedAtDesc(project.getId(), currUserId, pageable);
        return notifications.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getMyNotifications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        UUID currUserId = currentUserService.getCurrentUserId();
        Page<Notification> notifications = repository.findByRecipientIdOrderByCreatedAtDesc(currUserId, pageable);
        return notifications.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public UnreadCountResponse getUnreadCount() {
        UUID currUserId = currentUserService.getCurrentUserId();
        long count = repository.countByRecipientIdAndIsReadFalse(currUserId);
        return mapper.toRespone(count);
    }

    @Transactional
    public void markRead(String notificationId){
        UUID notificationUUID = validationService.parseNotificationId(notificationId);
        UUID currUserId = currentUserService.getCurrentUserId();
        Notification notification = accessService.getAccessibleNotification(notificationUUID,currUserId);
        if(notification.isRead()){
            return;
        }
        notification.setRead(true);
        notification.setReadAt(Instant.now());
    }

    @Transactional
    public void markAllRead(){
        UUID currUserId = currentUserService.getCurrentUserId();
        Instant now = Instant.now();
        repository.markAllRead(currUserId,now);
    }


}
