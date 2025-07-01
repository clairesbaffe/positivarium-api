package com.positivarium.api.repository;

import com.positivarium.api.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long>{

    Page<Article> findAllByIsPublishedTrueOrderByPublishedAtDesc(Pageable pageable);

    Page<Article> findByUserIdAndIsPublishedTrueOrderByPublishedAtDesc(Long userId, Pageable pageable);

    Page<Article> findByUserIdAndIsPublishedFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<Article> findAllByCategoryIdInAndIsPublishedTrueOrderByPublishedAtDesc(List<Long> categoryIds, Pageable pageable);

    Optional<Article> findByIdAndUserIdAndIsPublishedFalse(Long id, Long userId);

    @Query(
            value = "SELECT a.* FROM articles a " +
                    "JOIN users u ON a.user_id = u.id " +
                    "JOIN follows f ON f.publisher_id = u.id " +
                    "WHERE f.user_id = ?1 " +
                    "AND a.is_published = true",
            nativeQuery = true
    )
    Page<Article> findAllPublishedByFollowedPublishers(Long userId, Pageable pageable);


    Optional<Article> findByUserIdAndId(Long userId, Long articleId);

    Optional<Article> findByIdAndIsPublishedTrue(Long id);

    Optional<Article> findByUserIdAndIdAndIsPublishedFalse(Long userId, Long articleId);


    // LIKES
    @Query(
        value = "SELECT a.* FROM articles a " +
            "JOIN likes l ON l.article_id = a.id " +
            "WHERE l.user_id = ?1",
        countQuery = "SELECT COUNT(*) FROM articles a " +
            "JOIN likes l ON l.article_id = a.id " +
            "WHERE l.user_id = ?1",
        nativeQuery = true
    )
    Page<Article> findAllLikedByUser(Long userId, Pageable pageable);

    @Query(
            value = "SELECT COUNT(*) FROM likes " +
                    "WHERE article_id = ?1",
            nativeQuery = true
    )
    Long countLikesByArticleId(Long articleId);

    @Query(
            value = "SELECT EXISTS(" +
                    "SELECT 1 FROM likes " +
                    "WHERE user_id = ?1 AND article_id = ?2" +
                    ") AS liked",
            nativeQuery = true
    )
    Boolean userLikedArticle(Long userId, Long articleId);

    @Query(
            value = "SELECT * FROM articles " +
                    "WHERE is_published = true AND published_at IS NOT NULL " +
                    "ORDER BY published_at DESC " +
                    "LIMIT ?1",
            nativeQuery = true
    )
    List<Article> findPublishedTopArticles(int topArticlesNumber);
}
