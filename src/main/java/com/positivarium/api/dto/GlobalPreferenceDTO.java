package com.positivarium.api.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record GlobalPreferenceDTO(
        Long id,
        MoodDTO mood,
        Set<CategoryDTO> categories
){
}
