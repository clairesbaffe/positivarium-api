package com.positivarium.api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "article_reports")
public class ArticleReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "is_reviewed", nullable = false)
    private boolean isReviewed;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
}
