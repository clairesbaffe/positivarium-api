package com.positivarium.api.repository;

import com.positivarium.api.entity.Article;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long>{

    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Article> findByUserIdAndIsPublishedTrueOrderByPublishedAtDesc(Long userId, Pageable pageable);

    Page<Article> findByUserIdAndIsPublishedFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<Article> findAllByCategoryIdInAndIsPublishedTrueOrderByPublishedAtDesc(List<Long> categoryIds, Pageable pageable);

    Optional<Article> findByUserIdAndId(Long userId, Long articleId);
    @Transactional
    void deleteByUserIdAndId(Long userId, Long articleId);

    Optional<Article> findByIdAndIsPublishedTrue(Long id);
    @Transactional
    void deleteByIdAndIsPublishedTrue(Long id);

    @Query(
        value = "SELECT a.* FROM articles a " +
            "JOIN likes l ON l.article_id = a.id " +
            "WHERE l.user_id = ?1",
        countQuery = "SELECT COUNT(*) FROM articles a " +
            "JOIN likes l ON l.article_id = a.id " +
            "WHERE l.user_id = ?1",
        nativeQuery = true
    )
    public Page<Article> findAllLikedByUser(Long userId, Pageable pageable);

    @Query(
            value = "SELECT COUNT(*) FROM likes " +
                    "WHERE article_id = ?1",
            nativeQuery = true
    )
    public Long countLikesByArticleId(Long articleId);

    @Query(
            value = "SELECT EXISTS(" +
                    "SELECT 1 FROM likes " +
                    "WHERE user_id = ?1 AND article_id = ?2" +
                    ") AS liked",
            nativeQuery = true
    )
    public Boolean userLikedArticle(Long userId, Long articleId);

}
