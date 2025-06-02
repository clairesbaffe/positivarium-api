package com.positivarium.api.controller;

import com.positivarium.api.dto.PublisherRequestDTO;
import com.positivarium.api.dto.UserDTO;
import com.positivarium.api.service.FollowService;
import com.positivarium.api.service.PublisherRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final PublisherRequestService publisherRequestService;
    private final FollowService followService;

    @PreAuthorize("!hasRole('BAN')")
    @PostMapping("/publisher_request")
    public void createPublisherRequest(
            @RequestBody PublisherRequestDTO publisherRequestDTO,
            Authentication authentication
    ){
        publisherRequestService.createPublisherRequest(publisherRequestDTO, authentication);
    }

    // Banned user can still cancel publisher request
    @PostMapping("/publisher_request/cancel/{id}")
    public void cancelPublisherRequest(
            @PathVariable Long id,
            Authentication authentication
    ){
        publisherRequestService.cancelPublisherRequest(id, authentication);
    }

    @GetMapping("/publisher_request")
    public Page<PublisherRequestDTO> getPublisherRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){
        return publisherRequestService.getPublisherRequestsByUser(page, size, authentication);
    }


    @PreAuthorize("!hasRole('BAN')")
    @PostMapping("/follow/{publisherId}")
    public void followPublisher(
            @PathVariable Long publisherId,
            Authentication authentication
    ){
        followService.followPublisher(publisherId, authentication);
    }

    // Banned user can still unfollow
    @DeleteMapping("/follow/{publisherId}")
    public void unfollowPublisher(
            @PathVariable Long publisherId,
            Authentication authentication
    ){
        followService.unfollowPublisher(publisherId, authentication);
    }

    @GetMapping("/follow")
    public Page<UserDTO> getFollowing(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){
        return followService.getFollowing(page, size, authentication);
    }
}
