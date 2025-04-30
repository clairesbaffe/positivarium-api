package com.positivarium.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "news_categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private GeneralCategory generalCategory;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Article> articles = new ArrayList<>();
}
