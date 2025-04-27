package com.positivarium.api.repository;

import com.positivarium.api.entity.Article;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long>{

    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Article> findByUserIdAndIsPublishedTrueOrderByPublishedAtDesc(Long userId, Pageable pageable);

    Page<Article> findByUserIdAndIsPublishedFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Optional<Article> findByUserIdAndId(Long userId, Long articleId);
    @Transactional
    void deleteByUserIdAndId(Long userId, Long articleId);

    Optional<Article> findByIdAndIsPublishedTrue(Long id);
    @Transactional
    void deleteByIdAndIsPublishedTrue(Long id);

}
