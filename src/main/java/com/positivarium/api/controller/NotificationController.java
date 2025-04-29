package com.positivarium.api.controller;

import com.positivarium.api.dto.NotificationDTO;
import com.positivarium.api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/{userId}")
    public void sendNotification(
            @PathVariable Long userId,
            @RequestBody NotificationDTO notificationDTO,
            Authentication authentication
    ){
        notificationService.sendNotification(userId, notificationDTO, authentication);
    }

    @GetMapping("/sent")
    public Page<NotificationDTO> getSentNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){
        return notificationService.getSentNotifications(page, size, authentication);
    }

    @GetMapping("/received")
    public Page<NotificationDTO> getReceivedNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){
        return notificationService.getReceivedNotifications(page, size, authentication);
    }

    @GetMapping("/received/unread")
    public Page<NotificationDTO> getUnreadReceivedNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){
        return notificationService.getUnreadReceivedNotifications(page, size, authentication);
    }

    @PutMapping("/{id}")
    public void markNotificationAsRead(
            @PathVariable Long id,
            Authentication authentication
    ){
        try{
            notificationService.markNotificationAsRead(id, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
