package com.positivarium.api.dto;

import lombok.Builder;

@Builder
public record MoodDTO(
        Long id,
        String name,
        String type
) {
}
