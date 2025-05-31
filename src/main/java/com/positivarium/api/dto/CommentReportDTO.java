package com.positivarium.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentReportDTO(
        Long id,
        String reason,
        LocalDateTime createdAt,
        boolean isReviewed
) {
}
