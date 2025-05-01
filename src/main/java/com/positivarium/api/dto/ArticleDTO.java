package com.positivarium.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ArticleDTO(
        Long id,
        String title,
        String content,
        String mainImage,
        String username,
        CategoryDTO category,
        boolean isPublished,
        LocalDateTime publishedAt,
        Long likesCount,
        Boolean userLiked
) {
}