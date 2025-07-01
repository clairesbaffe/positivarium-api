package com.positivarium.api.repository;

import com.positivarium.api.entity.DailyNewsPreference;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DailyPreferenceRepository extends CrudRepository<DailyNewsPreference, Long> {

    Optional<DailyNewsPreference> findByJournalEntryIdAndUserId(Long journalEntryId, Long userId);

    List<DailyNewsPreference> findByJournalEntryId(Long journalEntryId);

    List<DailyNewsPreference> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

}