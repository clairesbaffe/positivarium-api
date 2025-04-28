package com.positivarium.api.dto;

import lombok.Builder;

@Builder
public record ArticleReportWithArticleDTO(
        Long id,
        String reason,
        boolean isReviewed,
        SimpleArticleDTO article
) {
}
