package com.positivarium.api.repository;

import com.positivarium.api.entity.DailyNewsPreference;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DailyPreferenceRepository extends CrudRepository<DailyNewsPreference, Long> {

    Optional<DailyNewsPreference> findByJournalEntryIdAndUserId(Long journalEntryId, Long userId);

    List<DailyNewsPreference> findByJournalEntryId(Long journalEntryId);
}