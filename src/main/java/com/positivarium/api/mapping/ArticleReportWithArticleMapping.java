package com.positivarium.api.mapping;

import com.positivarium.api.dto.ArticleReportWithArticleDTO;
import com.positivarium.api.entity.Article;
import com.positivarium.api.entity.ArticleReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleReportWithArticleMapping {

    private final SimpleArticleMapping simpleArticleMapping;

    public ArticleReportWithArticleDTO entityToDto(ArticleReport articleReport, Article article){
        return ArticleReportWithArticleDTO.builder()
                .id(articleReport.getId())
                .reason(articleReport.getReason())
                .isReviewed(articleReport.isReviewed())
                .createdAt(articleReport.getCreatedAt())
                .article(simpleArticleMapping.entityToDto(article))
                .build();
    }
}
