package com.positivarium.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record JournalEntryDTO(
        Long id,
        String description,
        Set<MoodDTO> moods,
        Set<CategoryDTO> categories,
        LocalDateTime createdAt
) {
}
