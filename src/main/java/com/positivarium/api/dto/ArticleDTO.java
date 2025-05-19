package com.positivarium.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleDTO(
        Long id,
        String title,
        String description,
        String content,
        String mainImage,
        String username,
        CategoryDTO category,
        boolean isPublished,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime publishedAt,
        Long likesCount,
        Boolean userLiked
) {
}