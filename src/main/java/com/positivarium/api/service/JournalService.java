package com.positivarium.api.service;

import com.positivarium.api.dto.JournalEntryDTO;
import com.positivarium.api.dto.JournalEntryRequestDTO;
import com.positivarium.api.dto.MoodDTO;
import com.positivarium.api.entity.*;
import com.positivarium.api.exception.EntryWasAlreadyCreatedTodayException;
import com.positivarium.api.exception.ResourceNotFoundException;
import com.positivarium.api.mapping.CategoryMapping;
import com.positivarium.api.mapping.JournalEntryMapping;
import com.positivarium.api.mapping.MoodMapping;
import com.positivarium.api.repository.CategoryRepository;
import com.positivarium.api.repository.JournalEntryRepository;
import com.positivarium.api.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalEntryRepository journalEntryRepository;
    private final JournalEntryMapping journalEntryMapping;
    private final UserService userService;
    private final MoodRepository moodRepository;
    private final MoodMapping moodMapping;
    private final DailyPreferenceService dailyPreferenceService;
    private final CategoryRepository categoryRepository;
    private final CategoryMapping categoryMapping;

    public void createEntry(JournalEntryRequestDTO journalEntryDTO, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);

        // check if one was already created today
        Optional<JournalEntry> lastJournalEntry = journalEntryRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId());
        lastJournalEntry.ifPresent(entry -> {
            if (entry.getCreatedAt().toLocalDate().isEqual(LocalDate.now()))
                throw new EntryWasAlreadyCreatedTodayException();
        });

        JournalEntry journalEntry = journalEntryMapping.dtoToEntity(journalEntryDTO, user);
        journalEntryRepository.save(journalEntry);

        dailyPreferenceService.checkAndSaveDailyPreferenceFromJournalEntry(user, journalEntry, journalEntryDTO, false);
    }

    public Page<JournalEntryDTO> getAllEntries(int pageNumber, int pageSize, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<JournalEntry> journalEntries = journalEntryRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId(), pageable);

        return journalEntries.map(journalEntry -> {
            List<DailyNewsPreference> dailyNewsPreferences = dailyPreferenceService.getDailyPreferenceByJournalEntryId(journalEntry.getId());
            Set<Category> categories = dailyNewsPreferences.stream()
                    .flatMap(pref -> pref.getCategories().stream())
                    .collect(Collectors.toSet());
            return journalEntryMapping.entityToDtoWithCategories(journalEntry, categories);
        });
    }

    public JournalEntryDTO getEntryById(Long id, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);

        JournalEntry journalEntry = journalEntryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found"));
        List<DailyNewsPreference> dailyNewsPreferences = dailyPreferenceService.getDailyPreferenceByJournalEntryId(id);
        Set<Category> categories = dailyNewsPreferences.stream()
                .flatMap(dailyNewsPreference -> dailyNewsPreference.getCategories().stream())
                .collect(Collectors.toSet());
        return journalEntryMapping.entityToDtoWithCategories(journalEntry, categories);
    }

    public void updateEntry(Long id, JournalEntryRequestDTO journalEntryDTO, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);

        JournalEntry journalEntry = journalEntryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found"));
        journalEntryMapping.updateEntityFromDto(journalEntryDTO, journalEntry);
        journalEntryRepository.save(journalEntry);

        dailyPreferenceService.checkAndSaveDailyPreferenceFromJournalEntry(user, journalEntry, journalEntryDTO, true);
    }

    public void deleteEntry(Long id, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);

        JournalEntry journalEntry = journalEntryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found"));
        journalEntryRepository.delete(journalEntry);
    }

    public List<MoodDTO> getAllMoods() {
        Iterable<Mood> moods = moodRepository.findAll();

        List<MoodDTO> moodDTOs = new ArrayList<>();
        for (Mood mood : moods) {
            moodDTOs.add(moodMapping.entityToDto(mood));
        }

        return moodDTOs;
    }

    public JournalEntryDTO todaysEntry(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);

        // Get last entry from user
        Optional<JournalEntry> lastJournalEntry = journalEntryRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId());

        // Check if it was today
        JournalEntry todaysJournalEntry = lastJournalEntry
                .filter(entry -> entry.getCreatedAt().toLocalDate().isEqual(LocalDate.now()))
                .orElse(null);

        if (todaysJournalEntry != null) {
            List<DailyNewsPreference> dailyNewsPreferences =
                    dailyPreferenceService.getDailyPreferenceByJournalEntryId(lastJournalEntry.get().getId());
            Set<Category> categories = dailyNewsPreferences.stream()
                    .flatMap(pref -> pref.getCategories().stream())
                    .collect(Collectors.toSet());

            return journalEntryMapping.entityToDtoWithCategories(todaysJournalEntry, categories);
        } else {
            return null;
        }
    }
}
