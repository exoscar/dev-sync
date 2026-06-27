package org.devsync.spring.notification.controller;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.constants.AppConstants;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.notification.dto.NotificationResponse;
import org.devsync.spring.notification.dto.UnreadCountResponse;
import org.devsync.spring.notification.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/workspaces/{workspaceId}")
    public ApiResponse<Page<NotificationResponse>> getMyWorkspaceNotifications(@PathVariable String workspaceId,
                                                                               @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE + "") int page,
                                                                               @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE + "") int size
    ) {
        Page<NotificationResponse> responses = notificationService.getMyWorkspaceNotifications(workspaceId, page, size);
        return ApiResponseUtil.success(responses);
    }

    @GetMapping("/projects/{projectId}")
    public ApiResponse<Page<NotificationResponse>> getMyProjectNotifications(@PathVariable String projectId,
                                                                             @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE + "") int page,
                                                                             @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE + "") int size
    ) {
        Page<NotificationResponse> responses = notificationService.getMyProjectNotifications(projectId, page, size);
        return ApiResponseUtil.success(responses);
    }

    @GetMapping()
    public ApiResponse<Page<NotificationResponse>> getMyNotifications(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE + "") int page,
                                                                      @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE + "") int size
    ) {
        Page<NotificationResponse> responses = notificationService.getMyNotifications(page, size);
        return ApiResponseUtil.success(responses);
    }

    @GetMapping("/unread-count")
    public ApiResponse<UnreadCountResponse> getUnreadCount() {
        UnreadCountResponse response = notificationService.getUnreadCount();
        return ApiResponseUtil.success(response);
    }

    @PatchMapping("/{notificationId}/read")
    public ApiResponse<Void> markRead(@PathVariable String notificationId){
        notificationService.markRead(notificationId);
        return ApiResponseUtil.success("Read notification");
    }

    @PatchMapping("/read-all")
    public ApiResponse<Void> markAllRead(){
        notificationService.markAllRead();
        return ApiResponseUtil.success("Read all notifications");
    }




}
