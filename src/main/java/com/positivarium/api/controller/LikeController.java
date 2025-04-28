package com.positivarium.api.controller;

import com.positivarium.api.dto.SimpleArticleDTO;
import com.positivarium.api.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping("/articles")
    public Page<SimpleArticleDTO> getLikedArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){
        return likeService.getLikedArticles(page, size, authentication);
    }
}
