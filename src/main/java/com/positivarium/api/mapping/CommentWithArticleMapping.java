package com.positivarium.api.mapping;

import com.positivarium.api.dto.CommentWithArticleDTO;
import com.positivarium.api.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentWithArticleMapping {

    private final SimpleArticleMapping articleMapping;

    public CommentWithArticleDTO entityToDto(Comment comment){
        String username = (comment.getUser() == null || comment.getUser().getRoles().stream()
                .anyMatch(role -> "ROLE_BAN".equals(role.getName())))
                ? "Auteur inconnu"
                : comment.getUser().getUsername();

        return CommentWithArticleDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(username)
                .createdAt(comment.getCreatedAt())
                .article(articleMapping.entityToDto(comment.getArticle()))
                .build();
    }
}
