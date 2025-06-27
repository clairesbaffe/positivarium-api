package com.positivarium.api.mapping;

import com.positivarium.api.dto.HttpLogDTO;
import com.positivarium.api.entity.HttpLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HttpLogMapping {

    private final UserWithRolesMapping userWithRolesMapping;

    public HttpLogDTO entityToDto(HttpLog httpLog){
        return HttpLogDTO.builder()
                .id(httpLog.getId())
                .method(httpLog.getMethod())
                .uri(httpLog.getUri())
                .status(httpLog.getStatus())
                .durationMs(httpLog.getDurationMs())
                .timestamp(httpLog.getTimestamp())
                .user(httpLog.getUser() != null ? userWithRolesMapping.entityToDto(httpLog.getUser()) : null)
                .build();
    }
}
