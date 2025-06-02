package com.positivarium.api.service;

import com.positivarium.api.dto.NotificationDTO;
import com.positivarium.api.entity.Notification;
import com.positivarium.api.entity.User;
import com.positivarium.api.exception.ResourceNotFoundException;
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
        User sender = userService.getCurrentUser(authentication);
        User receiver = userService.findUserById(userId);

        Notification notification = notificationMapping.dtoToEntity(notificationDTO);
        notification.setRead(false);
        notification.setSender(sender);
        notification.setReceiver(receiver);

        notificationRepository.save(notification);

        // send email here ?
    }

    public Page<NotificationDTO> getSentNotifications(int pageNumber, int pageSize, Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Notification> notifications = notificationRepository.findAllBySenderId(user.getId(), pageable);
        return notifications.map(notificationMapping::entityToDto);
    }

    public Page<NotificationDTO> getReceivedNotifications(int pageNumber, int pageSize, Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Notification> notifications = notificationRepository.findAllByReceiverId(user.getId(), pageable);
        return notifications.map(notificationMapping::entityToDto);
    }

    public Page<NotificationDTO> getUnreadReceivedNotifications(int pageNumber, int pageSize, Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Notification> notifications = notificationRepository.findAllByReceiverIdAndIsReadFalse(user.getId(), pageable);
        return notifications.map(notificationMapping::entityToDto);
    }

    public void markNotificationAsRead(Long id, Authentication authentication){
        User user = userService.getCurrentUser(authentication);

        Notification notification = notificationRepository.findByIdAndReceiverIdAndIsReadFalse(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
