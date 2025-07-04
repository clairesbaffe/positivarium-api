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
        String username = (article.getUser() == null || article.getUser().getRoles().stream()
            .anyMatch(role -> "ROLE_BAN".equals(role.getName())))
                ? "Auteur inconnu"
                : article.getUser().getUsername();

        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .content(article.getContent())
                .mainImage(article.getMainImage())
                .username(username)
                .category(article.getCategory() != null ? categoryMapping.entityToDto(article.getCategory()) : null)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
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
                .description(articleDTO.description())
                .content(articleDTO.content())
                .mainImage(articleDTO.mainImage())
                .category(category)
                .isPublished(articleDTO.isPublished())
                .publishedAt(articleDTO.publishedAt())
                .build();
    }

    public void updateEntityFromDto(ArticleDTO articleDTO, Article article){
        Category category = (articleDTO.category() != null && articleDTO.category().id() != null) ?
                categoryRepository.findById(articleDTO.category().id())
                        .orElseThrow(() -> new RuntimeException("Category not found")) : null;

        // nothing returned because article is a reference, not a copy of the object
        article.setTitle(articleDTO.title());
        article.setDescription(articleDTO.description());
        article.setContent(articleDTO.content());
        article.setMainImage(articleDTO.mainImage());
        article.setCategory(category);
    }

    public ArticleDTO entityToDtoWithLikes(Article article, Long likesCount, Boolean userLiked){
        String username = (article.getUser() == null || article.getUser().getRoles().stream()
            .anyMatch(role -> "ROLE_BAN".equals(role.getName())))
                ? "Auteur inconnu"
                : article.getUser().getUsername();


        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .content(article.getContent())
                .mainImage(article.getMainImage())
                .username(username)
                .category(article.getCategory() != null ? categoryMapping.entityToDto(article.getCategory()) : null)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .isPublished(article.isPublished())
                .publishedAt(article.getPublishedAt())
                .likesCount(likesCount != null ? likesCount : 0)
                .userLiked(userLiked)
                .build();
    }
}
