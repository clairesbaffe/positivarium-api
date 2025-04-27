package com.positivarium.api.controller;

import com.positivarium.api.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/article/{articleId}")
    public void like(
            @PathVariable Long articleId,
            Authentication authentication
    ){
        likeService.like(articleId, authentication);
    }

    @DeleteMapping("/article/{articleId}")
    public void unlike(
            @PathVariable Long articleId,
            Authentication authentication
    ){
        likeService.unlike(articleId, authentication);
    }
}
