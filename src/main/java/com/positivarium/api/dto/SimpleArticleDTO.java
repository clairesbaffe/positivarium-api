package com.positivarium.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SimpleArticleDTO(
        Long id,
        String title,
        String description,
        String mainImage,
        String username,
        CategoryDTO category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime publishedAt,
        Long likesCount
) {
}
