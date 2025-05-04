package com.positivarium.api.dto;

import java.util.Set;

public record JournalEntryRequestDTO(
        String description,
        Set<Long> moodIds,
        Set<Long> categoryIds
) {
}
