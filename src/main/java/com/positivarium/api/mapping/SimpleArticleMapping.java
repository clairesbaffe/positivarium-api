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
        return SimpleArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .main_image(article.getMainImage())
                .category(article.getCategory() != null ? categoryMapping.entityToDto(article.getCategory()) : null)
                .build();
    }

    public SimpleArticleDTO entityToDtoWithLikesCount(Article article, Long likesCount){
        return SimpleArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .main_image(article.getMainImage())
                .category(article.getCategory() != null ? categoryMapping.entityToDto(article.getCategory()) : null)
                .likesCount(likesCount)
                .build();
    }
}
