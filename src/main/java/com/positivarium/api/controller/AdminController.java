package com.positivarium.api.controller;

import com.positivarium.api.dto.*;
import com.positivarium.api.enums.PublisherRequestStatusEnum;
import com.positivarium.api.service.ArticleService;
import com.positivarium.api.service.CommentService;
import com.positivarium.api.service.PublisherRequestService;
import com.positivarium.api.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ReportService reportService;
    private final PublisherRequestService publisherRequestService;
    private final ArticleService articleService;
    private final CommentService commentService;

    @PreAuthorize("!hasRole('BAN')")
    @DeleteMapping("/articles/{id}")
    public void deletePublishedArticleById(@PathVariable Long id){
        articleService.deletePublishedArticle(id);
    }

    @PreAuthorize("!hasRole('BAN')")
    @DeleteMapping("/comments/{id}")
    public void deleteCommentById(@PathVariable Long id){
        commentService.deleteAnyComment(id);
    }

    @PreAuthorize("!hasRole('BAN')")
    @PostMapping("/reports/articles/{id}")
    public void markArticleReportAsRead(
            @PathVariable Long id
    ){
        reportService.markArticleReportAsRead(id);
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
        return reportService.getArticleReportById(id);
    }


    @PreAuthorize("!hasRole('BAN')")
    @PostMapping("/reports/comments/{id}")
    public void markCommentReportAsRead(
            @PathVariable Long id
    ){
        reportService.markCommentReportAsRead(id);
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
        return reportService.getCommentReportById(id);
    }


    @GetMapping("/publisher_requests")
    public Page<PublisherRequestDTO> getPublisherRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return publisherRequestService.getPublisherRequests(page, size);
    }

    @GetMapping("/publisher_requests/active")
    public Page<PublisherRequestDTO> getActivePublisherRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return publisherRequestService.getActivePublisherRequests(page, size);
    }

    @GetMapping("/publisher_requests/{id}")
    public PublisherRequestDTO getPublisherRequestById(
            @PathVariable Long id
    ){
        return publisherRequestService.getById(id);
    }

    @PreAuthorize("!hasRole('BAN')")
    @PostMapping("publisher_requests/{id}")
    public void updatePublisherRequestStatus(
            @PathVariable Long id,
            @RequestParam PublisherRequestStatusEnum status
    ){
        try{
            publisherRequestService.updatePublisherRequestStatus(id, status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
