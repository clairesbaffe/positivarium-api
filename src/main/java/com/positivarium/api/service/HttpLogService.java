package com.positivarium.api.service;

import com.positivarium.api.dto.HttpLogDTO;
import com.positivarium.api.entity.HttpLog;
import com.positivarium.api.entity.User;
import com.positivarium.api.mapping.HttpLogMapping;
import com.positivarium.api.repository.HttpLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class HttpLogService {

    private final HttpLogRepository httpLogRepository;
    private final UserService userService;
    private final HttpLogMapping httpLogMapping;

    public void log(String method, String uri, int status, long durationMs, String username) {
        User user = userService.findUserByUsername(username);

        httpLogRepository.save(
                HttpLog.builder()
                    .method(method)
                    .uri(uri)
                    .status(status)
                    .durationMs(durationMs)
                    .timestamp(Instant.now())
                    .user(user)
                    .build()
        );
    }

    public Page<HttpLogDTO> getLogs(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<HttpLog> httpLogs = httpLogRepository.findAll(pageable);
        return httpLogs.map(httpLogMapping::entityToDto);
    }
}
