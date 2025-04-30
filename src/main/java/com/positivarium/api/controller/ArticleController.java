package com.positivarium.api.controller;

import com.positivarium.api.dto.ArticleDTO;
import com.positivarium.api.dto.SimpleArticleDTO;
import com.positivarium.api.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/")
    public Page<SimpleArticleDTO> getArticles(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ){
        // manage depending on user role and journal status
        // potentially use credentials to get user roles, but credentials must not be mandatory
        return articleService.getArticles(page, size);
    }

    @GetMapping("/{id}")
    public ArticleDTO getArticleById(
        @PathVariable Long id,
        Authentication authentication
    ){
        // only returns article, for comments use getCommentsByArticleId
        try{
            return articleService.getArticleById(id, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/")
    public ArticleDTO createArticle(
            @RequestBody ArticleDTO articleDTO,
            Authentication authentication
    ){
        return articleService.createArticle(articleDTO, authentication);
    }

    // this route works for everyone (even not connected),
    // publishers that want to get the list of their published articles
    // and users that want to get the list of published article from a publisher
    @GetMapping("/published/{username}")
    public Page<SimpleArticleDTO> getPublishedArticlesByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            // username of the targeted publisher must be given
            @PathVariable String username
    ){
        // manage depending on user role and journal status
        // potentially use credentials to get user roles, but credentials must not be mandatory
        return articleService.getPublishedArticlesByUser(page, size, username);
    }

    @GetMapping("/categories")
    public Page<SimpleArticleDTO> getByCategoryIds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam List<Long> categoryIds
    ){
        return articleService.getByCategoryIds(page, size, categoryIds);
    }

    @GetMapping("/followed")
    public Page<SimpleArticleDTO> getPublishedFollowedPublishersArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){
        return articleService.getPublishedFollowedPublishersArticles(page, size, authentication);
    }

    @PostMapping("/publish/{id}")
    public void publishArticle(@PathVariable Long id, Authentication authentication){
        try{
            articleService.publishArticle(id, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id, Authentication authentication){
        // if role is publisher, check if article is owned by current user
        // if role is admin, do not allow drafts delete
        // which one needs to be checked before the other in case a user has both ?
        try{
            articleService.deleteArticle(id, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
