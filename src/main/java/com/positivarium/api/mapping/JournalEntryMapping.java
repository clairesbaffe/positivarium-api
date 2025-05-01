package com.positivarium.api.mapping;

import com.positivarium.api.dto.JournalEntryDTO;
import com.positivarium.api.entity.JournalEntry;
import com.positivarium.api.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JournalEntryMapping {

    public JournalEntry dtoToEntity(JournalEntryDTO journalEntryDTO, User user){
        return JournalEntry.builder()
                .description(journalEntryDTO.description())
                .user(user)
                .build();
    }

    public JournalEntryDTO entityToDto(JournalEntry journalEntry){
        return JournalEntryDTO.builder()
                .id(journalEntry.getId())
                .description(journalEntry.getDescription())
                .createdAt(journalEntry.getCreatedAt())
                .build();
    }

    public void updateEntityFromDto(JournalEntryDTO journalEntryDTO, JournalEntry journalEntry){
        journalEntry.setDescription(journalEntryDTO.description());
    }
}
