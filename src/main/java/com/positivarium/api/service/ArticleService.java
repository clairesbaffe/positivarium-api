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

import java.time.LocalDateTime;
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
        Page<Article> articles = articleRepository.findAllByOrderByCreatedAtDesc(pageable);

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

    public ArticleDTO getArticleById(Long id) throws Exception {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new Exception("Article not found"));
        System.out.println(article.getId());

        Long likesCount = articleRepository.countLikesByArticleId(id);

        return articleMapping.entityToDtoWithLikes(article, likesCount);
    }

    public Article findArticleById(Long id) throws Exception {
        return articleRepository.findById(id)
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

    public void updateArticle(Long id, ArticleDTO articleDTO) throws Exception {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new Exception("Article not found"));

        articleMapping.updateEntityFromDto(articleDTO, article);
        articleRepository.save(article);
    }

    public void deleteArticle(Long id, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return;

        List<String> roles = userService.getUserRoles(username);

        if(roles.contains("ROLE_ADMIN")){
            // admin can only delete published articles
            articleRepository.findByIdAndIsPublishedTrue(id)
                    .orElseThrow(() -> new Exception("Article not found"));
            articleRepository.deleteByIdAndIsPublishedTrue(id);
        } else { // ROLE_PUBLISHER
            // publisher can only delete their own articles
            User user = userService.getUser(username);
            if (user != null) {
                Long userId = user.getId();
                articleRepository.findByUserIdAndId(userId, id)
                        .orElseThrow(() -> new Exception("Article not found"));
                articleRepository.deleteByUserIdAndId(userId, id);
            }
        }
    }
}
