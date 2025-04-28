package com.positivarium.api.service;

import com.positivarium.api.dto.ArticleReportDTO;
import com.positivarium.api.dto.ArticleReportWithArticleDTO;
import com.positivarium.api.dto.CommentReportDTO;
import com.positivarium.api.dto.CommentReportWithCommentDTO;
import com.positivarium.api.entity.Article;
import com.positivarium.api.entity.ArticleReport;
import com.positivarium.api.entity.Comment;
import com.positivarium.api.entity.CommentReport;
import com.positivarium.api.mapping.ArticleReportMapping;
import com.positivarium.api.mapping.ArticleReportWithArticleMapping;
import com.positivarium.api.mapping.CommentReportMapping;
import com.positivarium.api.mapping.CommentReportWithCommentMapping;
import com.positivarium.api.repository.ArticleReportRepository;
import com.positivarium.api.repository.CommentReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ArticleReportRepository articleReportRepository;
    private final ArticleReportMapping articleReportMapping;
    private final ArticleService articleService;
    private final ArticleReportWithArticleMapping articleReportWithArticleMapping;
    private final CommentReportMapping commentReportMapping;
    private final CommentService commentService;
    private final CommentReportRepository commentReportRepository;
    private final CommentReportWithCommentMapping commentReportWithCommentMapping;

    public void createArticleReport(Long articleId, ArticleReportDTO articleReportDTO){
        try {
            Article article = articleService.findArticleById(articleId);
            ArticleReport articleReport = articleReportMapping.dtoToEntityWithArticle(articleReportDTO, article);
            articleReportRepository.save(articleReport);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createCommentReport(Long commentId, CommentReportDTO commentReportDTO){
        try {
            Comment comment = commentService.findCommentById(commentId);
            CommentReport commentReport = commentReportMapping.dtoToEntityWithComment(commentReportDTO, comment);
            commentReportRepository.save(commentReport);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void markArticleReportAsRead(Long id) throws Exception {
        ArticleReport articleReport = articleReportRepository.findById(id)
                .orElseThrow(() -> new Exception("Article report not found"));
        articleReport.setReviewed(true);
        articleReportRepository.save(articleReport);
    }

    public Page<ArticleReportWithArticleDTO> getAllArticleReportsNotReviewed(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ArticleReport> articleReports = articleReportRepository.findAllByIsReviewedFalseOrderByCreatedAtDesc(pageable);
        return articleReports.map(articleReport -> {
            try{
                Article article = articleService.findArticleById(articleReport.getArticle().getId());
                return articleReportWithArticleMapping.entityToDto(articleReport, article);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public ArticleReportWithArticleDTO getArticleReportById(Long id) throws Exception {
        ArticleReport articleReport = articleReportRepository.findById(id)
                .orElseThrow(() -> new Exception("Article report not found"));
        Article article = articleService.findArticleById(articleReport.getArticle().getId());
        return articleReportWithArticleMapping.entityToDto(articleReport, article);
    }


    public void markCommentReportAsRead(Long id) throws Exception {
        CommentReport commentReport = commentReportRepository.findById(id)
                .orElseThrow(() -> new Exception("Comment report not found"));
        commentReport.setReviewed(true);
        commentReportRepository.save(commentReport);
    }

    public Page<CommentReportWithCommentDTO> getAllCommentReportsNotReviewed(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CommentReport> commentReports = commentReportRepository.findAllByIsReviewedFalseOrderByCreatedAtDesc(pageable);
        return commentReports.map(commentReport -> {
            try {
                Comment comment = commentService.findCommentById(commentReport.getComment().getId());
                return commentReportWithCommentMapping.entityToDto(commentReport, comment);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CommentReportWithCommentDTO getCommentReportById(Long id) throws Exception {
        CommentReport commentReport = commentReportRepository.findById(id)
                .orElseThrow(() -> new Exception("Comment report not found"));
        Comment comment = commentService.findCommentById(commentReport.getComment().getId());
        return commentReportWithCommentMapping.entityToDto(commentReport, comment);
    }
}
