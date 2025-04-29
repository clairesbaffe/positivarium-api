package com.positivarium.api.controller;

import com.positivarium.api.dto.*;
import com.positivarium.api.enums.PublisherRequestStatusEnum;
import com.positivarium.api.service.PublisherRequestService;
import com.positivarium.api.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ReportService reportService;
    private final PublisherRequestService publisherRequestService;

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
        try{
            return publisherRequestService.getById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("publisher_requests/{id}")
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
