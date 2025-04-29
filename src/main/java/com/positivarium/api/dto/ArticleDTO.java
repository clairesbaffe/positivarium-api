package com.positivarium.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleDTO(
        Long id,
        String title,
        String content,
        String main_image,
        boolean isPublished,
        LocalDateTime publishedAt,
        Long likesCount,
        Boolean userLiked
) {
}