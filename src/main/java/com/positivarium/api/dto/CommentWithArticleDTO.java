package com.positivarium.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentWithArticleDTO(
        Long id,
        String content,
        String username,
        LocalDateTime createdAt,
        SimpleArticleDTO article
) {
}
