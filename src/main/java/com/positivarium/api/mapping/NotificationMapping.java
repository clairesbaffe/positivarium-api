package com.positivarium.api.mapping;

import com.positivarium.api.dto.NotificationDTO;
import com.positivarium.api.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapping {

    public Notification dtoToEntity(NotificationDTO notificationDTO){
        return Notification.builder()
                .title(notificationDTO.title())
                .content(notificationDTO.content())
                .build();
    }

    public NotificationDTO entityToDto(Notification notification){
        return  NotificationDTO.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
