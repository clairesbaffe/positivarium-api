package com.positivarium.api.service;

import com.positivarium.api.dto.NotificationDTO;
import com.positivarium.api.entity.Notification;
import com.positivarium.api.entity.User;
import com.positivarium.api.mapping.NotificationMapping;
import com.positivarium.api.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapping notificationMapping;
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    public void sendNotification(
            Long userId,
            NotificationDTO notificationDTO,
            Authentication authentication
    ){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return;

        User sender = userService.getUser(username);
        User receiver = userService.findUserById(userId);

        Notification notification = notificationMapping.dtoToEntity(notificationDTO);
        notification.setRead(false);
        notification.setSender(sender);
        notification.setReceiver(receiver);

        notificationRepository.save(notification);

        // send email here ?
    }

    public Page<NotificationDTO> getSentNotifications(int pageNumber, int pageSize, Authentication authentication){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return null;

        User user = userService.getUser(username);
        Long userId = user.getId();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Notification> notifications = notificationRepository.findAllBySenderId(userId, pageable);
        return notifications.map(notificationMapping::entityToDto);
    }

    public Page<NotificationDTO> getReceivedNotifications(int pageNumber, int pageSize, Authentication authentication){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return null;

        User user = userService.getUser(username);
        Long userId = user.getId();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Notification> notifications = notificationRepository.findAllByReceiverId(userId, pageable);
        return notifications.map(notificationMapping::entityToDto);
    }

    public Page<NotificationDTO> getUnreadReceivedNotifications(int pageNumber, int pageSize, Authentication authentication){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return null;

        User user = userService.getUser(username);
        Long userId = user.getId();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Notification> notifications = notificationRepository.findAllByReceiverIdAndIsReadFalse(userId, pageable);
        return notifications.map(notificationMapping::entityToDto);
    }

    public void markNotificationAsRead(Long id, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return;

        User user = userService.getUser(username);
        Long userId = user.getId();

        Notification notification = notificationRepository.findByIdAndReceiverIdAndIsReadFalse(id, userId)
                .orElseThrow(() -> new Exception("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
