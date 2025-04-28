package com.positivarium.api.controller;

import com.positivarium.api.dto.ArticleReportDTO;
import com.positivarium.api.dto.ArticleReportWithArticleDTO;
import com.positivarium.api.dto.CommentReportDTO;
import com.positivarium.api.dto.CommentReportWithCommentDTO;
import com.positivarium.api.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ReportService reportService;

    @PutMapping("/reports/articles/{id}")
    public void markArticleReportAsRead(
            @PathVariable Long id
    ){
        try{
            reportService.markArticleReportAsRead(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/reports/articles/")
    public Page<ArticleReportWithArticleDTO> getAllArticleReportNotReviewed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return reportService.getAllArticleReportsNotReviewed(page, size);
    }

    @GetMapping("/reports/articles/{id}")
    public ArticleReportWithArticleDTO getArticleReportById(
            @PathVariable Long id
    ){
        try{
            return reportService.getArticleReportById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PutMapping("/reports/comments/{id}")
    public void markCommentReportAsRead(
            @PathVariable Long id
    ){
        try{
            reportService.markCommentReportAsRead(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/reports/comments/")
    public Page<CommentReportWithCommentDTO> getAllCommentReportNotReviewed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return reportService.getAllCommentReportsNotReviewed(page, size);
    }

    @GetMapping("/reports/comments/{id}")
    public CommentReportWithCommentDTO getCommentReportById(
            @PathVariable Long id
    ){
        try{
            return reportService.getCommentReportById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
