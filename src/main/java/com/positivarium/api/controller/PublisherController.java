package com.positivarium.api.controller;

import com.positivarium.api.dto.ArticleDTO;
import com.positivarium.api.dto.SimpleArticleDTO;
import com.positivarium.api.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publisher")
@RequiredArgsConstructor
public class PublisherController {

    private final ArticleService articleService;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body("This is ok, you can get in");
    }

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

    @PostMapping("/articles/")
    public ArticleDTO createArticle(
            @RequestBody ArticleDTO articleDTO,
            Authentication authentication
    ){
        return articleService.createArticle(articleDTO, authentication);
    }

    @PostMapping("/articles/publish/{id}")
    public void publishArticle(@PathVariable Long id, Authentication authentication){
        try{
            articleService.publishArticle(id, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/articles/{id}")
    public void updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleDTO articleDTO,
            Authentication authentication
    ){
        try{
            articleService.updateArticle(id, articleDTO, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
