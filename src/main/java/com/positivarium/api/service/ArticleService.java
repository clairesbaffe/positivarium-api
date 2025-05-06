package com.positivarium.api.service;

import com.positivarium.api.dto.ArticleDTO;
import com.positivarium.api.dto.SimpleArticleDTO;
import com.positivarium.api.entity.Article;
import com.positivarium.api.entity.User;
import com.positivarium.api.mapping.ArticleMapping;
import com.positivarium.api.mapping.SimpleArticleMapping;
import com.positivarium.api.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final SimpleArticleMapping simpleArticleMapping;
    private final ArticleMapping articleMapping;
    private final UserService userService;

    public Page<SimpleArticleDTO> getArticles(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Article> articles = articleRepository.findAllByIsPublishedTrueOrderByPublishedAtDesc(pageable);

        return articles.map(article -> {
            Long likesCount = articleRepository.countLikesByArticleId(article.getId());
            return simpleArticleMapping.entityToDtoWithLikesCount(article, likesCount);
        });
    }

    public Page<SimpleArticleDTO> getPublishedArticlesByUser(int pageNumber, int pageSize, String username){
        if (username == null) return null;

        User user = userService.getUser(username);
        if (user == null) return null;

        Long userId = user.getId();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Article> articles = articleRepository.findByUserIdAndIsPublishedTrueOrderByPublishedAtDesc(userId, pageable);

        return articles.map(article -> {
            Long likesCount = articleRepository.countLikesByArticleId(article.getId());
            return simpleArticleMapping.entityToDtoWithLikesCount(article, likesCount);
        });
    }

    public Page<SimpleArticleDTO> getDraftsByUser(int pageNumber, int pageSize, Authentication authentication){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return null;

        User user = userService.getUser(username);
        if (user == null) return null;

        Long userId = user.getId();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Article> articles = articleRepository.findByUserIdAndIsPublishedFalseOrderByCreatedAtDesc(userId, pageable);
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
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return null;

        User user = userService.getUser(username);
        if (user == null) return null;

        Long userId = user.getId();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Article> articles = articleRepository.findAllPublishedByFollowedPublishers(userId, pageable);
        return articles.map(article -> {
            Long likesCount = articleRepository.countLikesByArticleId(article.getId());
            return simpleArticleMapping.entityToDtoWithLikesCount(article, likesCount);
        });
    }

    public ArticleDTO getArticleById(Long id, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;

        Boolean liked = (username != null && userService.getUser(username) != null)
                ? articleRepository.userLikedArticle(userService.getUser(username).getId(), id)
                : false;

        Article article = articleRepository.findByIdAndIsPublishedTrue(id)
                .orElseThrow(() -> new Exception("Article not found"));

        Long likesCount = articleRepository.countLikesByArticleId(id);

        return articleMapping.entityToDtoWithLikes(article, likesCount, liked);
    }

    public ArticleDTO getDraftByIdAndByUser(Long id, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return null;

        User user = userService.getUser(username);
        Long userId = user.getId();

        Article article = articleRepository.findByIdAndUserIdAndIsPublishedFalse(id, userId)
                .orElseThrow(() -> new Exception("Article not found"));

        return articleMapping.entityToDto(article);
    }

    public Article findArticleById(Long id) throws Exception {
        return articleRepository.findById(id)
                .orElseThrow(() -> new Exception("Article not found"));
    }

    public Article findPublishedArticleById(Long id) throws Exception {
        return articleRepository.findByIdAndIsPublishedTrue(id)
                .orElseThrow(() -> new Exception("Article not found"));
    }

    public ArticleDTO createArticle(ArticleDTO articleDTO, Authentication authentication) {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return null;

        User user = userService.getUser(username);

        Article article = articleMapping.dtoToEntity(articleDTO);
        article.setUser(user);
        article.setPublished(false); // article is automatically created as draft, cannot be published when being created
        article.setPublishedAt(null);

        Article savedArticle = articleRepository.save(article);
        return articleMapping.entityToDto(savedArticle);
    }

    public void publishArticle(Long id, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return;

        User user = userService.getUser(username);
        if (user == null) return;

        Long userId = user.getId();

        Article article = articleRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new Exception("Article not found"));

        article.setPublished(true);

        articleRepository.save(article);
    }

    public void updateArticle(Long id, ArticleDTO articleDTO, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return;

        User user = userService.getUser(username);
        Long userId = user.getId();

        Article article = articleRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new Exception("Article not found"));

        articleMapping.updateEntityFromDto(articleDTO, article);
        articleRepository.save(article);
    }

    public void updateDraft(Long id, ArticleDTO articleDTO, Authentication authentication) throws Exception {
        User user = userService.getCurrentUser(authentication);
        Article article = articleRepository.findByUserIdAndIdAndIsPublishedFalse(user.getId(), id)
                .orElseThrow(() -> new Exception("Article not found"));

        articleMapping.updateEntityFromDto(articleDTO, article);
        articleRepository.save(article);
    }

    public void deletePublishedArticle(Long id) throws Exception {
        // ONLY FOR ADMINS
        Article article = articleRepository.findByIdAndIsPublishedTrue(id)
                .orElseThrow(() -> new Exception("Article not found"));
        articleRepository.delete(article);
    }

    public void deleteArticle(Long id, Authentication authentication) throws Exception {
        // ONLY FOR NOT BANNED PUBLISHERS
        User user = userService.getCurrentUser(authentication);
        Article article = articleRepository.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new Exception("Article not found"));
        articleRepository.delete(article);
    }

    public void deleteDraft(Long id, Authentication authentication) throws Exception {
        // ONLY FOR PUBLISHERS
        User user = userService.getCurrentUser(authentication);
        Article article = articleRepository.findByUserIdAndIdAndIsPublishedFalse(user.getId(), id)
                .orElseThrow(() -> new Exception("Article not found"));
        articleRepository.delete(article);
    }
}
