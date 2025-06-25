package com.positivarium.api.service;

import com.positivarium.api.entity.HttpLog;
import com.positivarium.api.entity.User;
import com.positivarium.api.repository.HttpLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class HttpLogService {

    private final HttpLogRepository httpLogRepository;
    private final UserService userService;

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
}
