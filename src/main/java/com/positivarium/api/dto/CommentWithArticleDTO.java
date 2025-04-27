package com.positivarium.api.dto;

import lombok.Builder;

@Builder
public record CommentWithArticleDTO(
        Long id,
        String content,
        String username,
        SimpleArticleDTO article
) {
}
