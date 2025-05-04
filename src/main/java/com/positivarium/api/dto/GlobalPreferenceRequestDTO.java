package com.positivarium.api.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record GlobalPreferenceRequestDTO(
        Long moodId,
        Set<Long> categoryIds
) {
}
