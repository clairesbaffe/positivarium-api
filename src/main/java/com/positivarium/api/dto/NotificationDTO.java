package com.positivarium.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationDTO(
        Long id,
        String title,
        String content,
        Boolean isRead,
        LocalDateTime createdAt
) {
}
