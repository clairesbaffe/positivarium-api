package com.positivarium.api.service;

import com.positivarium.api.dto.CommentDTO;
import com.positivarium.api.dto.CommentWithArticleDTO;
import com.positivarium.api.entity.Comment;
import com.positivarium.api.entity.User;
import com.positivarium.api.mapping.CommentMapping;
import com.positivarium.api.mapping.CommentWithArticleMapping;
import com.positivarium.api.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentWithArticleMapping commentWithArticleMapping;
    private final CommentMapping commentMapping;
    private final UserService userService;
    private final ArticleService articleService;

    public Page<CommentWithArticleDTO> getAllComments(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Comment> comments = commentRepository.findAllByOrderByCreatedAtDesc(pageable);
        return comments.map(commentWithArticleMapping::entityToDto);
    }

    public CommentWithArticleDTO getCommentWithArticle(Long id) throws Exception {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new Exception("Comment not found"));
        return commentWithArticleMapping.entityToDto(comment);
    }

    public Comment findCommentById(Long id) throws Exception {
        return commentRepository.findById(id)
                .orElseThrow(() -> new Exception("Comment not found"));
    }

    public void createComment(CommentDTO commentDTO, Long articleId, Authentication authentication) throws Exception{
        User user = userService.getCurrentUser(authentication);

        try {
            Comment comment = commentMapping.dtoToEntity(commentDTO);
            comment.setUser(user);
            comment.setArticle(articleService.findPublishedArticleById(articleId));

            commentRepository.save(comment);
        } catch (Exception e) {
            throw new Exception("Unable to create comment because article was not found", e);
        }
    }

    public Page<CommentDTO> getArticleComments(int pageNumber, int pageSize, Long articleId){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Comment> comments = commentRepository.findAllByArticleId(pageable, articleId);
        return comments.map(commentMapping::entityToDto);
    }

    public Page<CommentWithArticleDTO> getUserComments(int pageNumber, int pageSize, String username){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Comment> comments = commentRepository.findAllByUserUsername(pageable, username);
        return comments.map(commentWithArticleMapping::entityToDto);
    }

    public void deleteAnyComment(Long id) throws Exception {
        // ONLY FOR ADMIN
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new Exception("Comment not found"));
        commentRepository.delete(comment);
    }

    public void deleteOwnComment(Long id, Authentication authentication) throws Exception {
        User user = userService.getCurrentUser(authentication);
        Comment comment = commentRepository.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new Exception("Comment not found"));
        commentRepository.delete(comment);
    }
}
