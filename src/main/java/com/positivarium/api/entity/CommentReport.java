package com.positivarium.api.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name = "comment_reports")
public class CommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "is_reviewed", nullable = false)
    private boolean isReviewed;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

}
