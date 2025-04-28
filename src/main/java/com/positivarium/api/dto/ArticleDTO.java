package com.positivarium.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
public record ArticleDTO(
        Long id,
        String title,
        String content,
        String main_image,
        boolean isPublished,
        LocalDateTime publishedAt,
        Long likesCount
) {
}