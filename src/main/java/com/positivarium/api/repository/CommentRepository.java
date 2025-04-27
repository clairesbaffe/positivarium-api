package com.positivarium.api.repository;

import com.positivarium.api.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    Page<Comment> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Comment> findAllByArticleId(Pageable pageable, Long articleId);

    Page<Comment> findAllByUserUsername(Pageable pageable, String username);

    Optional<Comment> findByUserUsernameAndId(String username, Long id);
    @Transactional
    void deleteByUserUsernameAndId(String username, Long articleId);

}
