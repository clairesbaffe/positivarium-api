package com.positivarium.api.controller;

import com.positivarium.api.dto.ArticleReportDTO;
import com.positivarium.api.dto.CommentReportDTO;
import com.positivarium.api.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PreAuthorize("!hasRole('BAN')")
    @PostMapping("/articles/{articleId}")
    public void createArticleReport(
            @PathVariable Long articleId,
            @RequestBody ArticleReportDTO articleReportDTO
    ){
        reportService.createArticleReport(articleId, articleReportDTO);
    }

    @PreAuthorize("!hasRole('BAN')")
    @PostMapping("/comments/{commentId}")
    public void createCommentReport(
            @PathVariable Long commentId,
            @RequestBody CommentReportDTO commentReportDTO
    ){
        reportService.createCommentReport(commentId, commentReportDTO);
    }
}
