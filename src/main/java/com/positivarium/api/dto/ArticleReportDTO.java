package com.positivarium.api.dto;

import lombok.Builder;

@Builder
public record ArticleReportDTO(
        Long id,
        String reason,
        boolean isReviewed
) {
}
