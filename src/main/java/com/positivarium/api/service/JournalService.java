package com.positivarium.api.service;

import com.positivarium.api.dto.JournalEntryDTO;
import com.positivarium.api.dto.JournalEntryRequestDTO;
import com.positivarium.api.entity.JournalEntry;
import com.positivarium.api.entity.User;
import com.positivarium.api.mapping.JournalEntryMapping;
import com.positivarium.api.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalEntryRepository journalEntryRepository;
    private final JournalEntryMapping journalEntryMapping;
    private final UserService userService;

    public void createEntry(JournalEntryRequestDTO journalEntryDTO, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return;

        User user = userService.getUser(username);
        Long userId = user.getId();

        // check if one was already created today
        Optional<JournalEntry> lastJournalEntry = journalEntryRepository.findTopByUserIdOrderByCreatedAtDesc(userId);
        lastJournalEntry.ifPresent(entry -> {
            if (entry.getCreatedAt().toLocalDate().isEqual(LocalDate.now())) {
                throw new RuntimeException("An entry was already created today");
            }
        });

        JournalEntry journalEntry = journalEntryMapping.dtoToEntity(journalEntryDTO, user);
        journalEntryRepository.save(journalEntry);
    }

    public Page<JournalEntryDTO> getAllEntries(int pageNumber, int pageSize, Authentication authentication){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return null;

        User user = userService.getUser(username);
        Long userId = user.getId();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<JournalEntry> journalEntries = journalEntryRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
        return journalEntries.map(journalEntryMapping::entityToDto);
    }

    public JournalEntryDTO getEntryById(Long id, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return null;

        User user = userService.getUser(username);
        Long userId = user.getId();

        JournalEntry journalEntry = journalEntryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new Exception("Entry not found"));
        return journalEntryMapping.entityToDto(journalEntry);
    }

    public void updateEntry(Long id, JournalEntryRequestDTO journalEntryDTO, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return;

        User user = userService.getUser(username);
        Long userId = user.getId();

        JournalEntry journalEntry = journalEntryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new Exception("Entry not found"));
        journalEntryMapping.updateEntityFromDto(journalEntryDTO, journalEntry);
        journalEntryRepository.save(journalEntry);
    }

    public void deleteEntry(Long id, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return;

        User user = userService.getUser(username);
        Long userId = user.getId();

        journalEntryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new Exception("Entry not found"));
        journalEntryRepository.deleteByIdAndUserId(id, userId);
    }
}
