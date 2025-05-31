package com.positivarium.api.service;

import com.positivarium.api.dto.ArticleDTO;
import com.positivarium.api.dto.CategoryDTO;
import com.positivarium.api.dto.SimpleArticleDTO;
import com.positivarium.api.entity.Article;
import com.positivarium.api.entity.Category;
import com.positivarium.api.entity.User;
import com.positivarium.api.exception.ResourceNotFoundException;
import com.positivarium.api.mapping.ArticleMapping;
import com.positivarium.api.mapping.CategoryMapping;
import com.positivarium.api.mapping.SimpleArticleMapping;
import com.positivarium.api.repository.ArticleRepository;
import com.positivarium.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final SimpleArticleMapping simpleArticleMapping;
    private final ArticleMapping articleMapping;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final CategoryMapping categoryMapping;

    public Page<SimpleArticleDTO> getArticles(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Article> articles = articleRepository.findAllByIsPublishedTrueOrderByPublishedAtDesc(pageable);

        return articles.map(article -> {
            Long likesCount = articleRepository.countLikesByArticleId(article.getId());
            return simpleArticleMapping.entityToDtoWithLikesCount(article, likesCount);
        });
    }

    public Page<SimpleArticleDTO> getPublishedArticlesByUser(int pageNumber, int pageSize, String username){
        User user = userService.getUser(username);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Article> articles = articleRepository.findByUserIdAndIsPublishedTrueOrderByPublishedAtDesc(user.getId(), pageable);
        return articles.map(article -> {
            Long likesCount = articleRepository.countLikesByArticleId(article.getId());
            return simpleArticleMapping.entityToDtoWithLikesCount(article, likesCount);
        });
    }

    public Page<SimpleArticleDTO> getDraftsByUser(int pageNumber, int pageSize, Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Article> articles = articleRepository.findByUserIdAndIsPublishedFalseOrderByCreatedAtDesc(user.getId(), pageable);
        return articles.map(simpleArticleMapping::entityToDto);
    }

    public Page<SimpleArticleDTO> getByCategoryIds(int pageNumber, int pageSize, List<Long> categoryIds){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Article> articles = articleRepository.findAllByCategoryIdInAndIsPublishedTrueOrderByPublishedAtDesc(categoryIds, pageable);
        return articles.map(article -> {
            Long likesCount = articleRepository.countLikesByArticleId(article.getId());
            return simpleArticleMapping.entityToDtoWithLikesCount(article, likesCount);
        });
    }

    public Page<SimpleArticleDTO> getPublishedFollowedPublishersArticles(int pageNumber, int pageSize, Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Article> articles = articleRepository.findAllPublishedByFollowedPublishers(user.getId(), pageable);
        return articles.map(article -> {
            Long likesCount = articleRepository.countLikesByArticleId(article.getId());
            return simpleArticleMapping.entityToDtoWithLikesCount(article, likesCount);
        });
    }

    public ArticleDTO getArticleById(Long id, Authentication authentication) {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;

        // Not calling getCurrentUser because user does not need to be authenticated
        Boolean liked = (username != null && userService.getUser(username) != null)
                ? articleRepository.userLikedArticle(userService.getUser(username).getId(), id)
                : false;

        Article article = articleRepository.findByIdAndIsPublishedTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        Long likesCount = articleRepository.countLikesByArticleId(id);
        return articleMapping.entityToDtoWithLikes(article, likesCount, liked);
    }

    public ArticleDTO getDraftByIdAndByUser(Long id, Authentication authentication){
        User user = userService.getCurrentUser(authentication);

        Article article = articleRepository.findByIdAndUserIdAndIsPublishedFalse(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        return articleMapping.entityToDto(article);
    }

    public Article findArticleById(Long id){
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
    }

    public Article findPublishedArticleById(Long id){
        return articleRepository.findByIdAndIsPublishedTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
    }

    public ArticleDTO createArticle(ArticleDTO articleDTO, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);

        Article article = articleMapping.dtoToEntity(articleDTO);
        article.setUser(user);
        article.setPublished(false); // article is automatically created as draft, cannot be published when being created
        article.setPublishedAt(null);

        Article savedArticle = articleRepository.save(article);
        return articleMapping.entityToDto(savedArticle);
    }

    public void publishArticle(Long id, Authentication authentication){
        User user = userService.getCurrentUser(authentication);

        Article article = articleRepository.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        article.setPublished(true);

        articleRepository.save(article);
    }

    public void updateArticle(Long id, ArticleDTO articleDTO, Authentication authentication){
        User user = userService.getCurrentUser(authentication);

        Article article = articleRepository.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        articleMapping.updateEntityFromDto(articleDTO, article);
        articleRepository.save(article);
    }

    public void updateDraft(Long id, ArticleDTO articleDTO, Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        Article article = articleRepository.findByUserIdAndIdAndIsPublishedFalse(user.getId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        articleMapping.updateEntityFromDto(articleDTO, article);
        articleRepository.save(article);
    }

    public void deletePublishedArticle(Long id){
        // ONLY FOR ADMINS
        Article article = articleRepository.findByIdAndIsPublishedTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        articleRepository.delete(article);
    }

    public void deleteArticle(Long id, Authentication authentication){
        // ONLY FOR NOT BANNED PUBLISHERS
        User user = userService.getCurrentUser(authentication);
        Article article = articleRepository.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        articleRepository.delete(article);
    }

    public void deleteDraft(Long id, Authentication authentication){
        // ONLY FOR PUBLISHERS
        User user = userService.getCurrentUser(authentication);
        Article article = articleRepository.findByUserIdAndIdAndIsPublishedFalse(user.getId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        articleRepository.delete(article);
    }

    public List<CategoryDTO> getAllCategories(){
        Iterable<Category> categories = categoryRepository.findAll();

        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for (Category category : categories) {
            categoryDTOs.add(categoryMapping.entityToDto(category));
        }

        return categoryDTOs;
    }
}
