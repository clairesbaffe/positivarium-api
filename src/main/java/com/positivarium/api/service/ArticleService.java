package com.positivarium.api.service;

import com.positivarium.api.dto.ArticleDTO;
import com.positivarium.api.dto.CategoryDTO;
import com.positivarium.api.dto.SimpleArticleDTO;
import com.positivarium.api.entity.Article;
import com.positivarium.api.entity.Category;
import com.positivarium.api.entity.DailyNewsPreference;
import com.positivarium.api.entity.User;
import com.positivarium.api.exception.ResourceNotFoundException;
import com.positivarium.api.mapping.ArticleMapping;
import com.positivarium.api.mapping.CategoryMapping;
import com.positivarium.api.mapping.SimpleArticleMapping;
import com.positivarium.api.repository.ArticleRepository;
import com.positivarium.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final SimpleArticleMapping simpleArticleMapping;
    private final ArticleMapping articleMapping;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final CategoryMapping categoryMapping;
    private final DailyPreferenceService dailyPreferenceService;

    private static final int PERSONALISED_PAGES = 5;

    public Page<SimpleArticleDTO> getArticles(int pageNumber, int pageSize, Authentication authentication){
        Boolean personalise = pageNumber <= PERSONALISED_PAGES && authentication != null && authentication.isAuthenticated();
        if (Boolean.TRUE.equals(personalise) &&
                userService.getCurrentUser(authentication).getRoles()
                        .stream()
                        .anyMatch(role -> "ROLE_USER".equals(role.getName()))){
            Page<Article> articles = getPersonalisedArticles(pageNumber, pageSize, authentication);

            // articles == null means that user does not use journal
            // therefore, no personalisation is possible, use default feed
            if(articles != null){
                return articles.map(article -> {
                    Long likesCount = articleRepository.countLikesByArticleId(article.getId());
                    return simpleArticleMapping.entityToDtoWithLikesCount(article, likesCount);
                });
            }
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Article> articles = articleRepository.findAllByIsPublishedTrueOrderByPublishedAtDesc(pageable);

        return articles.map(article -> {
            Long likesCount = articleRepository.countLikesByArticleId(article.getId());
            return simpleArticleMapping.entityToDtoWithLikesCount(article, likesCount);
        });
    }

    public Page<Article> getPersonalisedArticles(int pageNumber, int pageSize, Authentication authentication){
        User user = userService.getCurrentUser(authentication);

        List<DailyNewsPreference> preferences = dailyPreferenceService.getDailyNewsPrefForLast3DaysByUserId(user.getId());
        if(preferences.isEmpty()) return null;

        // should contain every category and its score
        Map<Category, Integer> categoryScores = dailyPreferenceService.calculateCategoryScores(preferences);

        int topArticlesNumber = PERSONALISED_PAGES * pageSize;

        List<Article> topArticles = articleRepository.findPublishedTopArticles(topArticlesNumber)
                .stream()
                .sorted((a1, a2) -> {

                    double categoryScore1 = categoryScores.getOrDefault(a1.getCategory(), 1);
                    double categoryScore2 = categoryScores.getOrDefault(a2.getCategory(), 1);

                    double score1 = categoryScore1 * computeScore(a1);
                    double score2 = categoryScore2 * computeScore(a2);

                    return Double.compare(score2, score1); // DESC
                })
                .toList();

        // Paginate
        int start = Math.min(pageNumber * pageSize, topArticles.size());
        int end = Math.min(start + pageSize, topArticles.size());

        // Cuts list down to requested page
        List<Article> pagedArticles = topArticles.subList(start, end);

        // Turns List into Page
        return new PageImpl<>(pagedArticles, PageRequest.of(pageNumber, pageSize), topArticles.size());
    }

    private double computeScore(Article article) {
        double articleHalfLife = 72;

        double k = Math.log(2) / articleHalfLife;

        Duration duration = Duration.between(article.getPublishedAt(), LocalDateTime.now());
        double ageInHours = duration.toHours() + duration.toMinutesPart() / 60.0;

        return Math.exp(-k * ageInHours);
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
        Article article = articleRepository.findByIdAndIsPublishedTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;

        // Not calling getCurrentUser because user does not need to be authenticated
        Boolean liked = articleRepository.userLikedArticle(userService.getUser(username).getId(), id);

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
