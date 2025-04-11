package com.positivarium.api.dto;

import lombok.Builder;

@Builder
public record CommentReportDTO(
        Long id,
        String reason,
        boolean isReviewed
) {
}
