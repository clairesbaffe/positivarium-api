package com.positivarium.api.controller;

import com.positivarium.api.dto.CommentDTO;
import com.positivarium.api.dto.CommentWithArticleDTO;
import com.positivarium.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/")
    public Page<CommentWithArticleDTO> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return commentService.getAllComments(page, size);
    }

    @GetMapping("/{id}")
    public CommentWithArticleDTO getCommentWithArticle(
            @PathVariable Long id
    ){
        return commentService.getCommentWithArticle(id);
    }

    @PreAuthorize("!hasRole('BAN')")
    @PostMapping("/{articleId}")
    public void createComment(
            @RequestBody CommentDTO commentDTO,
            @PathVariable Long articleId,
            Authentication authentication
    ){
        try{
            commentService.createComment(commentDTO, articleId, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/article/{articleId}")
    public Page<CommentDTO> getArticleComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long articleId
    ){
        return commentService.getArticleComments(page, size, articleId);
    }

    @GetMapping("/user/{username}")
    public Page<CommentWithArticleDTO> getUserComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable String username
    ){
        return commentService.getUserComments(page, size, username);
    }

    @PreAuthorize("!hasRole('BAN')")
    @DeleteMapping("/{id}")
    public void deleteComment(
            @PathVariable Long id,
            Authentication authentication
    ){
        commentService.deleteOwnComment(id, authentication);
    }

}
