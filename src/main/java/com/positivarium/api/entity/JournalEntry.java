package com.positivarium.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "journal_entries")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "journal_entries_moods",
            joinColumns = @JoinColumn(name = "journal_entry_id"),
            inverseJoinColumns = @JoinColumn(name = "mood_id")
    )
    private Set<Mood> moods;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL)
    private List<DailyNewsPreference> dailyNewsPreferences = new ArrayList<>();
}
