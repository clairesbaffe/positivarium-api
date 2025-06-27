package com.positivarium.api.mapping;

import com.positivarium.api.dto.SimpleArticleDTO;
import com.positivarium.api.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleArticleMapping {

    private final CategoryMapping categoryMapping;

    public SimpleArticleDTO entityToDto(Article article){
        String username = (article.getUser() == null || article.getUser().getRoles().stream()
                .anyMatch(role -> "ROLE_BAN".equals(role.getName())))
                    ? "Auteur inconnu"
                    : article.getUser().getUsername();

        return SimpleArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .mainImage(article.getMainImage())
                .username(username)
                .category(article.getCategory() != null ? categoryMapping.entityToDto(article.getCategory()) : null)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .publishedAt(article.getPublishedAt())
                .build();
    }

    public SimpleArticleDTO entityToDtoWithLikesCount(Article article, Long likesCount){
        String username = (article.getUser() == null || article.getUser().getRoles().stream()
                .anyMatch(role -> "ROLE_BAN".equals(role.getName())))
                    ? "Auteur inconnu"
                    : article.getUser().getUsername();

        return SimpleArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .mainImage(article.getMainImage())
                .username(username)
                .category(article.getCategory() != null ? categoryMapping.entityToDto(article.getCategory()) : null)                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .publishedAt(article.getPublishedAt())
                .likesCount(likesCount != null ? likesCount : 0)
                .build();
    }
}
