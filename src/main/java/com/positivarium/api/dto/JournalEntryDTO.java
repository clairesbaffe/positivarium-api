package com.positivarium.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record JournalEntryDTO(
        Long id,
        String description,
        LocalDateTime date
) {
}
