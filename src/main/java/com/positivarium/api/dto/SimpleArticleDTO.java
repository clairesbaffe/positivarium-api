package com.positivarium.api.dto;

import lombok.Builder;

@Builder
public record SimpleArticleDTO(
        Long id,
        String title,
        String main_image,
        String username,
        CategoryDTO category,
        Long likesCount
) {
}
