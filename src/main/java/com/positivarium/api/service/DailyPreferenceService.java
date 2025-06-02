package com.positivarium.api.service;

import com.positivarium.api.dto.JournalEntryRequestDTO;
import com.positivarium.api.entity.*;
import com.positivarium.api.repository.CategoryRepository;
import com.positivarium.api.repository.DailyPreferenceRepository;
import com.positivarium.api.repository.GlobalPreferenceRepository;
import com.positivarium.api.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class DailyPreferenceService {

    private final DailyPreferenceRepository dailyPreferenceRepository;
    private final GlobalPreferenceRepository globalPreferenceRepository;
    private final CategoryRepository categoryRepository;
    private final MoodRepository moodRepository;

    public void checkAndSaveDailyPreferenceFromJournalEntry(
            User user,
            JournalEntry journalEntry,
            JournalEntryRequestDTO journalEntryRequestDTO,
            Boolean isUpdate
    ) {
        // CASE 1 : categories are specified in journal entry -> save daily preference
        // CASE 2 : categories are not specified but mood is :
            // CASE 2-1 : no match with global preferences -> do nothing (set to null if update)
            // CASE 2-2 : match with global preferences -> get categories and save daily preference
        // CASE 3 : categories and mood are not specified -> do nothing (set to null if update)

        if (journalEntryRequestDTO.categoryIds().isEmpty()) {
            if (journalEntryRequestDTO.moodIds().isEmpty()) {
                // CASE 3
                if (isUpdate) updateDailyPreference(user, journalEntry, null);
            } else {
                Iterable<GlobalNewsPreference> globalNewsPreferences = globalPreferenceRepository.findAllByUserId(user.getId());
                // CHECK GLOBAL WITH MOOD(S)
                if (globalNewsPreferences.iterator().hasNext()) {
                    Iterable<Mood> iterableMoods = moodRepository.findAllById(journalEntryRequestDTO.moodIds());
                    Set<Mood> moods = StreamSupport
                            .stream(iterableMoods.spliterator(), false)
                            .collect(Collectors.toSet());

                    // Get matched global preferences according to journal entry moods
                    List<GlobalNewsPreference> matchedGlobalPreferences = StreamSupport
                            .stream(globalNewsPreferences.spliterator(), false)
                            .filter(g -> moods.contains(g.getMood()))
                            .toList();

                    if (!matchedGlobalPreferences.isEmpty()) {
                        // Get all categories from matched global preferences
                        Set<Category> matchedCategories = matchedGlobalPreferences.stream()
                                .flatMap(pref -> pref.getCategories().stream())
                                .collect(Collectors.toSet());

                        // CASE 2-2
                        if (isUpdate) updateDailyPreference(user, journalEntry, matchedCategories);
                        else saveDailyPreference(user, journalEntry, matchedCategories);
                    } else {
                        // CASE 2-1
                        if (isUpdate) updateDailyPreference(user, journalEntry, null);
                    }
                }
            }
        } else {
            // CASE 1
            Iterable<Category> iterableCategories = categoryRepository.findAllById(journalEntryRequestDTO.categoryIds());
            Set<Category> categories = StreamSupport
                    .stream(iterableCategories.spliterator(), false)
                    .collect(Collectors.toSet());

            if (isUpdate) updateDailyPreference(user, journalEntry, categories);
            else saveDailyPreference(user, journalEntry, categories);
        }
    }

    public void updateDailyPreference(User user, JournalEntry journalEntry, Set<Category> categories) {
        Optional<DailyNewsPreference> previousDailyNewsPreference =
                dailyPreferenceRepository.findByJournalEntryIdAndUserId(journalEntry.getId(), user.getId());

        if (previousDailyNewsPreference.isPresent()) {
            DailyNewsPreference dailyNewsPreference = previousDailyNewsPreference.get();

            dailyNewsPreference.setCategories(categories);
            dailyPreferenceRepository.save(dailyNewsPreference);
        } else {
            saveDailyPreference(user, journalEntry, categories);
        }
    }

    public void saveDailyPreference(User user, JournalEntry journalEntry, Set<Category> categories) {
        DailyNewsPreference dailyNewsPreference =
                DailyNewsPreference.builder()
                        .user(user)
                        .journalEntry(journalEntry)
                        .categories(categories)
                        .build();

        dailyPreferenceRepository.save(dailyNewsPreference);
    }

    public List<DailyNewsPreference> getDailyPreferenceByJournalEntryId(Long id) {
        return dailyPreferenceRepository.findByJournalEntryId(id);
    }
}
