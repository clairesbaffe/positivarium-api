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
        return CommentWithArticleDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .article(articleMapping.entityToDto(comment.getArticle()))
                .build();
    }
}
