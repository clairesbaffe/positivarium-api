package com.positivarium.api.dto;

import com.positivarium.api.enums.PublisherRequestStatusEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PublisherRequestDTO(
        Long id,
        String motivation,
        PublisherRequestStatusEnum status,
        String username,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
