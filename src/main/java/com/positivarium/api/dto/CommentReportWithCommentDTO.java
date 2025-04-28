package com.positivarium.api.dto;

import lombok.Builder;

@Builder
public record CommentReportWithCommentDTO(
        Long id,
        String reason,
        boolean isReviewed,
        CommentWithArticleDTO comment
) {
}
