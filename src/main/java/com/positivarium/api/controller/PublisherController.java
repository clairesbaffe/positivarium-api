package com.positivarium.api.controller;

import com.positivarium.api.dto.ArticleDTO;
import com.positivarium.api.dto.SimpleArticleDTO;
import com.positivarium.api.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publisher")
@RequiredArgsConstructor
public class PublisherController {

    private final ArticleService articleService;

    @GetMapping("/articles/drafts")
    public Page<SimpleArticleDTO> getDraftsByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){

        // manage depending on user role and journal status
        // potentially use credentials to get user roles, but credentials must not be mandatory
        return articleService.getDraftsByUser(page, size, authentication);
    }

    @GetMapping("/articles/drafts/{id}")
    public ArticleDTO getDraftById(
            @PathVariable Long id,
            Authentication authentication
    ){
        return articleService.getDraftByIdAndByUser(id, authentication);
    }

    @PostMapping("/articles/")
    public ArticleDTO createArticle(
            @RequestBody ArticleDTO articleDTO,
            Authentication authentication
    ){
        return articleService.createArticle(articleDTO, authentication);
    }

    @PreAuthorize("!hasRole('BAN')")
    @PostMapping("/articles/publish/{id}")
    public void publishArticle(@PathVariable Long id, Authentication authentication){
        articleService.publishArticle(id, authentication);
    }

    @PreAuthorize("!hasRole('BAN')")
    @PutMapping("/articles/{id}")
    public void updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleDTO articleDTO,
            Authentication authentication
    ){
        articleService.updateArticle(id, articleDTO, authentication);
    }

    @PutMapping("/articles/drafts/{id}")
    public void updateDraft(
            @PathVariable Long id,
            @RequestBody ArticleDTO articleDTO,
            Authentication authentication
    ){
        articleService.updateDraft(id, articleDTO, authentication);
    }

    @PreAuthorize("!hasRole('BAN')")
    @DeleteMapping("/articles/{id}")
    public void deleteArticleById(
            @PathVariable Long id,
            Authentication authentication
    ){
        articleService.deleteArticle(id, authentication);
    }

    @DeleteMapping("/articles/drafts/{id}")
    public void deleteDraftById(
            @PathVariable Long id,
            Authentication authentication
    ){
        articleService.deleteDraft(id, authentication);
    }

}
