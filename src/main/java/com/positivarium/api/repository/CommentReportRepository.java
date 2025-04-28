package com.positivarium.api.repository;

import com.positivarium.api.entity.CommentReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CommentReportRepository extends CrudRepository<CommentReport, Long> {

    public Page<CommentReport> findAllByIsReviewedFalseOrderByCreatedAtDesc(Pageable pageable);
}
