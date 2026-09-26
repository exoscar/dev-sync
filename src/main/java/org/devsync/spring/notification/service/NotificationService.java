package org.devsync.spring.notification.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.cache.UnreadNotificationCache;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.notification.dto.CreateNotificationRequest;
import org.devsync.spring.notification.dto.NotificationResponse;
import org.devsync.spring.notification.dto.UnreadCountResponse;
import org.devsync.spring.notification.entity.Notification;
import org.devsync.spring.notification.event.UnreadNotificationEvent;
import org.devsync.spring.notification.mapper.NotificationMapper;
import org.devsync.spring.notification.repository.NotificationRepository;
import org.devsync.spring.project.entity.Project;
import org.devsync.spring.project.service.ProjectAccessService;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.service.WorkspaceAccessService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
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
    private final UnreadNotificationCache unreadNotificationCache;
    private final ApplicationEventPublisher applicationEventPublisher;

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
        Optional<Long> cache = unreadNotificationCache.get(currUserId);
        if (cache.isEmpty()) {
            long count = repository.countByRecipientIdAndIsReadFalse(currUserId);
            unreadNotificationCache.put(currUserId, count);
            return mapper.toRespone(count);
        }

        return mapper.toRespone(cache.get());
    }

    @Transactional
    public void markRead(String notificationId) {
        UUID notificationUUID = validationService.parseNotificationId(notificationId);
        UUID currUserId = currentUserService.getCurrentUserId();
        Notification notification = accessService.getAccessibleNotification(notificationUUID, currUserId);
        if (notification.isRead()) {
            return;
        }
        notification.setRead(true);
        notification.setReadAt(Instant.now());
        applicationEventPublisher.publishEvent(
                new UnreadNotificationEvent(
                        currUserId
                )
        );
    }

    @Transactional
    public void markAllRead() {
        UUID currUserId = currentUserService.getCurrentUserId();
        Instant now = Instant.now();
        repository.markAllRead(currUserId, now);
        applicationEventPublisher.publishEvent(
                new UnreadNotificationEvent(
                        currUserId
                )
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createNotification(CreateNotificationRequest request) {
        List<User> recipients = request.getRecipients();
        List<Notification> notifications = recipients.stream().map(user -> {
            Notification notification = new Notification();
            notification.setMessage(request.getMessage());
            notification.setTitle(request.getTitle());
            notification.setRecipient(user);
            notification.setProjectId(request.getProjectId());
            notification.setWorkspaceId(request.getWorkspaceId());
            notification.setResourceType(request.getResourceType());
            notification.setType(request.getNotificationType());
            notification.setResourceId(request.getResourceId());
            return notification;
        }).toList();

        repository.saveAll(notifications);
        repository.flush();
        recipients.forEach(user -> {
            applicationEventPublisher.publishEvent(
                    new UnreadNotificationEvent(user.getId())
            );
        });
    }


}
