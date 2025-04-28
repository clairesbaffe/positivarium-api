package com.positivarium.api.mapping;

import com.positivarium.api.dto.ArticleReportDTO;
import com.positivarium.api.dto.CommentReportDTO;
import com.positivarium.api.entity.Article;
import com.positivarium.api.entity.ArticleReport;
import com.positivarium.api.entity.Comment;
import com.positivarium.api.entity.CommentReport;
import org.springframework.stereotype.Component;

@Component
public class CommentReportMapping {

    public CommentReportDTO entityToDto(CommentReport commentReport){
        return CommentReportDTO.builder()
                .id(commentReport.getId())
                .reason(commentReport.getReason())
                .isReviewed(commentReport.isReviewed())
                .build();
    }

    public CommentReport dtoToEntityWithComment(CommentReportDTO commentReportDTO, Comment comment){
        return CommentReport.builder()
                .reason(commentReportDTO.reason())
                .isReviewed(commentReportDTO.isReviewed())
                .comment(comment)
                .build();
    }

}
