package com.positivarium.api.mapping;

import com.positivarium.api.dto.JournalEntryDTO;
import com.positivarium.api.dto.JournalEntryRequestDTO;
import com.positivarium.api.dto.MoodDTO;
import com.positivarium.api.entity.JournalEntry;
import com.positivarium.api.entity.Mood;
import com.positivarium.api.entity.User;
import com.positivarium.api.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class JournalEntryMapping {

    private final MoodMapping moodMapping;
    private final MoodRepository moodRepository;

    public JournalEntry dtoToEntity(JournalEntryRequestDTO journalEntryDTO, User user){
        Iterable<Mood> iterableMoods = moodRepository.findAllById(journalEntryDTO.moodIds());
        Set<Mood> moods = StreamSupport
                .stream(iterableMoods.spliterator(), false)
                .collect(Collectors.toSet());

        return JournalEntry.builder()
                .description(journalEntryDTO.description())
                .moods(moods)
                .user(user)
                .build();
    }

    public JournalEntryDTO entityToDto(JournalEntry journalEntry){
        Set<MoodDTO> moodDTOs = new HashSet<>(journalEntry.getMoods()).stream()
                .map(moodMapping::entityToDto)
                .collect(Collectors.toSet());

        return JournalEntryDTO.builder()
                .id(journalEntry.getId())
                .description(journalEntry.getDescription())
                .moods(moodDTOs)
                .createdAt(journalEntry.getCreatedAt())
                .build();
    }

    public void updateEntityFromDto(JournalEntryRequestDTO journalEntryDTO, JournalEntry journalEntry){
        Iterable<Mood> iterableMoods = moodRepository.findAllById(journalEntryDTO.moodIds());
        Set<Mood> moods = StreamSupport
                .stream(iterableMoods.spliterator(), false)
                .collect(Collectors.toSet());

        journalEntry.setDescription(journalEntryDTO.description());
        journalEntry.setMoods(moods);
    }
}
