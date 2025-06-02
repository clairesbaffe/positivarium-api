package com.positivarium.api.mapping;

import com.positivarium.api.dto.CommentDTO;
import com.positivarium.api.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapping {

    public CommentDTO entityToDto(Comment comment){
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public Comment dtoToEntity(CommentDTO commentDTO){
        return Comment.builder()
                .content(commentDTO.content())
                .build();
    }
}
