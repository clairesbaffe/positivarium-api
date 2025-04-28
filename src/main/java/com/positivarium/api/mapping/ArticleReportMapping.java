package com.positivarium.api.mapping;

import com.positivarium.api.dto.ArticleReportDTO;
import com.positivarium.api.entity.Article;
import com.positivarium.api.entity.ArticleReport;
import org.springframework.stereotype.Component;

@Component
public class ArticleReportMapping {

    public ArticleReportDTO entityToDto(ArticleReport articleReport){
        return ArticleReportDTO.builder()
                .id(articleReport.getId())
                .reason(articleReport.getReason())
                .isReviewed(articleReport.isReviewed())
                .build();
    }

    public ArticleReport dtoToEntityWithArticle(ArticleReportDTO articleReportDTO, Article article){
        return  ArticleReport.builder()
                .reason(articleReportDTO.reason())
                .isReviewed(articleReportDTO.isReviewed())
                .article(article)
                .build();
    }

}
