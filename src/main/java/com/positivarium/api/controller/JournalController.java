package com.positivarium.api.controller;

import com.positivarium.api.dto.JournalEntryDTO;
import com.positivarium.api.dto.JournalEntryRequestDTO;
import com.positivarium.api.dto.MoodDTO;
import com.positivarium.api.service.JournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journal")
@RequiredArgsConstructor
public class JournalController {

    private final JournalService journalService;

    @PostMapping("/")
    public void createEntry(
            @RequestBody JournalEntryRequestDTO journalEntryDTO,
            Authentication authentication
    ){
        journalService.createEntry(journalEntryDTO, authentication);
    }

    @GetMapping("/")
    public Page<JournalEntryDTO> getAllEntries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){
        return journalService.getAllEntries(page, size, authentication);
    }

    @GetMapping("/{id}")
    public JournalEntryDTO getEntryById(
            @PathVariable Long id,
            Authentication authentication
    ){
        return journalService.getEntryById(id, authentication);
    }

    @PatchMapping("/{id}")
    public void updateEntry(
            @PathVariable Long id,
            @RequestBody JournalEntryRequestDTO journalEntryDTO,
            Authentication authentication
    ){
        journalService.updateEntry(id, journalEntryDTO, authentication);
    }

    @DeleteMapping("/{id}")
    public void deleteEntry(
            @PathVariable Long id,
            Authentication authentication
    ){
        journalService.deleteEntry(id, authentication);
    }

    @GetMapping("/moods")
    public List<MoodDTO> getAllMoods(){
        return journalService.getAllMoods();
    }

    @GetMapping("/today")
    public JournalEntryDTO hasMadeEntryToday(Authentication authentication){
        return journalService.todaysEntry(authentication);
    }
}
