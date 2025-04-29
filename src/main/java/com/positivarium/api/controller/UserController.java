package com.positivarium.api.controller;

import com.positivarium.api.dto.PublisherRequestDTO;
import com.positivarium.api.service.PublisherRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final PublisherRequestService publisherRequestService;

    @PostMapping("/publisher_request")
    public void createPublisherRequest(
            @RequestBody PublisherRequestDTO publisherRequestDTO,
            Authentication authentication
    ){
        publisherRequestService.createPublisherRequest(publisherRequestDTO, authentication);
    }

    @PutMapping("/publisher_request/cancel/{id}")
    public void cancelPublisherRequest(
            @PathVariable Long id,
            Authentication authentication
    ){
        try{
            publisherRequestService.cancelPublisherRequest(id, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/publisher_request")
    public Page<PublisherRequestDTO> getPublisherRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){
        return publisherRequestService.getPublisherRequestsByUser(page, size, authentication);
    }
}
