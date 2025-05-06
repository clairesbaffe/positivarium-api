package com.positivarium.api.repository;

import com.positivarium.api.entity.ArticleReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ArticleReportRepository extends CrudRepository<ArticleReport, Long> {

    Page<ArticleReport> findAllByIsReviewedFalseOrderByCreatedAtDesc(Pageable pageable);
}
