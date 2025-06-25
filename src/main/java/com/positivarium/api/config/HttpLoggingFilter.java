package com.positivarium.api.config;

import com.positivarium.api.service.HttpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HttpLoggingFilter extends OncePerRequestFilter {

    @Autowired
    private HttpLogService httpLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String method = request.getMethod();
            String uri = request.getRequestURI();
            int status = response.getStatus();;

            String username =
                    request.getUserPrincipal() != null
                            ? request.getUserPrincipal().getName()
                            : null;

            httpLogService.log(method, uri, status, duration, username);
        }
    }


}
