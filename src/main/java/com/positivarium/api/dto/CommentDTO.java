package com.positivarium.api.dto;

import lombok.Builder;

@Builder
public record CommentDTO(
        Long id,
        String content
) {
}
