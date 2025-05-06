package com.positivarium.api.mapping;

import com.positivarium.api.dto.CommentReportWithCommentDTO;
import com.positivarium.api.entity.Comment;
import com.positivarium.api.entity.CommentReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentReportWithCommentMapping {

    private final CommentWithArticleMapping commentWithArticleMapping;

    public CommentReportWithCommentDTO entityToDto(CommentReport commentReport, Comment comment){
        return CommentReportWithCommentDTO.builder()
                .id(commentReport.getId())
                .reason(commentReport.getReason())
                .isReviewed(commentReport.isReviewed())
                .comment(commentWithArticleMapping.entityToDto(comment))
                .build();
    }

}
