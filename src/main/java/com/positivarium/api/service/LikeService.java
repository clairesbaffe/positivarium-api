package com.positivarium.api.service;

import com.positivarium.api.entity.Article;
import com.positivarium.api.entity.User;
import com.positivarium.api.repository.ArticleRepository;
import com.positivarium.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
}
