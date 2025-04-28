package com.positivarium.api.service;

import com.positivarium.api.dto.SimpleArticleDTO;
import com.positivarium.api.entity.Article;
import com.positivarium.api.entity.User;
import com.positivarium.api.mapping.SimpleArticleMapping;
import com.positivarium.api.repository.ArticleRepository;
import com.positivarium.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final ArticleService articleService;
    private final SimpleArticleMapping simpleArticleMapping;

    public void like(Long articleId, Authentication authentication){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return;

        User user = userService.getUser(username);
        if (user == null) return;

        try{
            Article article = articleService.findArticleById(articleId);

            user.getLikedArticles().add(article);
            article.getUsersWhoLiked().add(user);

            userRepository.save(user);
            articleRepository.save(article);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void unlike(Long articleId, Authentication authentication){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return;

        User user = userService.getUser(username);
        if (user == null) return;

        try{
            Article article = articleService.findArticleById(articleId);

            user.getLikedArticles().remove(article);
            article.getUsersWhoLiked().remove(user);

            userRepository.save(user);
            articleRepository.save(article);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Page<SimpleArticleDTO> getLikedArticles(int pageNumber, int pageSize, Authentication authentication){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return null;

        User user = userService.getUser(username);
        if (user == null) return null;

        Long userId = user.getId();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Article> articles = articleRepository.findAllLikedByUser(userId, pageable);
        return articles.map(simpleArticleMapping::entityToDto);
    }
}
