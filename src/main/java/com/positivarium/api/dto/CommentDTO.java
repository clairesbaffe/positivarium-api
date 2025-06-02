package com.positivarium.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentDTO(
        Long id,
        String content,
        String username,
        LocalDateTime createdAt
) {
}
