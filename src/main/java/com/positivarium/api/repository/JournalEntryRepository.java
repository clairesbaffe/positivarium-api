package com.positivarium.api.repository;

import com.positivarium.api.entity.JournalEntry;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JournalEntryRepository extends CrudRepository<JournalEntry, Long> {

    Optional<JournalEntry> findTopByUserIdOrderByCreatedAtDesc(Long userId);

    Page<JournalEntry> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Optional<JournalEntry> findByIdAndUserId(Long id, Long userId);

    @Transactional
    void deleteByIdAndUserId(Long id, Long userId);
}
