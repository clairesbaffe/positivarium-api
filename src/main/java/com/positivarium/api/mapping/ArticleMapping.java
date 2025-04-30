package com.positivarium.api.mapping;

import com.positivarium.api.dto.ArticleDTO;
import com.positivarium.api.entity.Article;
import com.positivarium.api.entity.Category;
import com.positivarium.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleMapping {

    private final CategoryMapping categoryMapping;
    private final CategoryRepository categoryRepository;

    public ArticleDTO entityToDto(Article article){
        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .mainImage(article.getMainImage())
                .category(article.getCategory() != null ? categoryMapping.entityToDto(article.getCategory()) : null)
                .isPublished(article.isPublished())
                .publishedAt(article.getPublishedAt())
                .build();
    }

    public Article dtoToEntity(ArticleDTO articleDTO){
        Category category = (articleDTO.category() != null && articleDTO.category().id() != null) ?
                categoryRepository.findById(articleDTO.category().id())
                        .orElseThrow(() -> new RuntimeException("Category not found")) : null;

        return Article.builder()
                .title(articleDTO.title())
                .content(articleDTO.content())
                .mainImage(articleDTO.mainImage())
                .category(category)
                .isPublished(articleDTO.isPublished())
                .publishedAt(articleDTO.publishedAt())
                .build();
    }

    public void updateEntityFromDto(ArticleDTO articleDTO, Article article){
        // nothing returned because article is a reference, not a copy of the object
        article.setTitle(articleDTO.title());
        article.setContent(articleDTO.content());
        article.setMainImage(articleDTO.mainImage());
    }

    public ArticleDTO entityToDtoWithLikes(Article article, Long likesCount, Boolean userLiked){
        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .mainImage(article.getMainImage())
                .category(article.getCategory() != null ? categoryMapping.entityToDto(article.getCategory()) : null)
                .isPublished(article.isPublished())
                .publishedAt(article.getPublishedAt())
                .likesCount(likesCount != null ? likesCount : 0)
                .userLiked(userLiked)
                .build();
    }
}
