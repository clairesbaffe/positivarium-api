package com.positivarium.api.entity;

import com.positivarium.api.enums.MoodType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@Table(name = "moods")
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    private MoodType type;

    @ManyToMany(mappedBy = "moods")
    private Set<JournalEntry> journalEntries = new HashSet<>();
}
