package com.positivarium.api.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record HttpLogDTO(
        Long id,
        String method,
        String uri,
        int status,
        Long durationMs,
        Instant timestamp,
        UserWithRolesDTO user
) {
}
