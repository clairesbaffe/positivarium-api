package com.positivarium.api.controller;

import com.positivarium.api.dto.JournalEntryDTO;
import com.positivarium.api.dto.JournalEntryRequestDTO;
import com.positivarium.api.service.JournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        try{
            journalService.createEntry(journalEntryDTO, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        try{
            return journalService.getEntryById(id, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/{id}")
    public void updateEntry(
            @PathVariable Long id,
            @RequestBody JournalEntryRequestDTO journalEntryDTO,
            Authentication authentication
    ){
        try{
            journalService.updateEntry(id, journalEntryDTO, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteEntry(
            @PathVariable Long id,
            Authentication authentication
    ){
        try{
            journalService.deleteEntry(id, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
