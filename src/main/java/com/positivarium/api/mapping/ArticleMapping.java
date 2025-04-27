package com.positivarium.api.mapping;

import com.positivarium.api.dto.ArticleDTO;
import com.positivarium.api.entity.Article;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapping {

    public ArticleDTO entityToDto(Article article){
        return ArticleDTO.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .main_image(article.getMainImage())
                .isPublished(article.isPublished())
                .publishedAt(article.getPublishedAt())
                .build();
    }

    public Article dtoToEntity(ArticleDTO articleDTO){
        return Article.builder()
                .title(articleDTO.title())
                .content(articleDTO.content())
                .mainImage(articleDTO.main_image())
                .isPublished(articleDTO.isPublished())
                .publishedAt(articleDTO.publishedAt())
                .build();
    }

    public void updateEntityFromDto(ArticleDTO articleDTO, Article article){
        // nothing returned because article is a reference, not a copy of the object
        article.setTitle(articleDTO.title());
        article.setContent(articleDTO.content());
        article.setMainImage(articleDTO.main_image());
    }
}
