package com.positivarium.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentReportWithCommentDTO(
        Long id,
        String reason,
        boolean isReviewed,
        LocalDateTime createdAt,
        CommentWithArticleDTO comment
) {
}
