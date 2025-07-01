package com.positivarium.api.service;

import com.positivarium.api.dto.JournalEntryRequestDTO;
import com.positivarium.api.entity.*;
import com.positivarium.api.repository.CategoryRepository;
import com.positivarium.api.repository.DailyPreferenceRepository;
import com.positivarium.api.repository.GlobalPreferenceRepository;
import com.positivarium.api.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
                doNothingOrUpdate(isUpdate, user, journalEntry, null);
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
                        createOrUpdate(isUpdate, user, journalEntry, matchedCategories);
                    } else {
                        // CASE 2-1
                        doNothingOrUpdate(isUpdate, user, journalEntry, null);
                    }
                }
            }
        } else {
            // CASE 1
            Iterable<Category> iterableCategories = categoryRepository.findAllById(journalEntryRequestDTO.categoryIds());
            Set<Category> categories = StreamSupport
                    .stream(iterableCategories.spliterator(), false)
                    .collect(Collectors.toSet());

            createOrUpdate(isUpdate, user, journalEntry, categories);
        }
    }

    public void doNothingOrUpdate(Boolean isUpdate, User user, JournalEntry journalEntry, Set<Category> categories){
        if (Boolean.TRUE.equals(isUpdate)) updateDailyPreference(user, journalEntry, categories);
    }
    public void createOrUpdate(Boolean isUpdate, User user, JournalEntry journalEntry, Set<Category> categories){
        if (Boolean.TRUE.equals(isUpdate)) updateDailyPreference(user, journalEntry, categories);
        else saveDailyPreference(user, journalEntry, categories);
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

    public List<DailyNewsPreference> getDailyNewsPrefForLast3DaysByUserId(Long userId){
        LocalDateTime threeDaysAgo = LocalDate.now().minusDays(3).atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().plusDays(1).atStartOfDay().minusSeconds(1);

        return dailyPreferenceRepository.findByUserIdAndCreatedAtBetween(userId, threeDaysAgo, todayEnd);
    }

    public Map<Category, Integer> calculateCategoryScores(List<DailyNewsPreference> preferences){
        LocalDate today = LocalDate.now();

        // check if preference was entered today
        boolean hasTodayPreference = preferences.stream()
                .anyMatch(p -> p.getCreatedAt().toLocalDate().isEqual(today));

        LocalDate day1 = hasTodayPreference ? today : today.minusDays(1);
        LocalDate day2 = day1.minusDays(1);
        LocalDate day3 = day2.minusDays(1);

        // Weight days, not 3, 2, 1 because default category score is 1
        Map<LocalDate, Integer> dayWeightMap = new HashMap<>();
        dayWeightMap.put(day1, 4);
        dayWeightMap.put(day2, 3);
        dayWeightMap.put(day3, 2);

        Map<Category, Integer> scoreMap = new HashMap<>();

        preferences.forEach(pref -> {
                LocalDate prefDate = pref.getCreatedAt().toLocalDate();
                Integer weight = dayWeightMap.get(prefDate);
                if (weight == null) return;

                // for each category :
                // creates in map if not exists + sets weigh
                // if exists, sums weight
                pref.getCategories().forEach(category -> scoreMap.merge(category, weight, Integer::sum));
            });

        // set all categories that were not in preferences to 1
        Iterable<Category> allCategories = categoryRepository.findAll();
        allCategories.forEach(category -> scoreMap.putIfAbsent(category, 1));

        return scoreMap;
    }
}
