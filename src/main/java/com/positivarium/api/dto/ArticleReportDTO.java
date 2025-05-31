package com.positivarium.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleReportDTO(
        Long id,
        String reason,
        boolean isReviewed,
        LocalDateTime createdAt
) {
}
