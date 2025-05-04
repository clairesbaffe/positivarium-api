package com.positivarium.api.controller;

import com.positivarium.api.dto.GlobalPreferenceDTO;
import com.positivarium.api.dto.GlobalPreferenceRequestDTO;
import com.positivarium.api.service.GlobalPreferencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/global_preferences")
@RequiredArgsConstructor
public class GlobalPreferencesController {

    private final GlobalPreferencesService globalPreferencesService;

    @PostMapping("/")
    public void addGlobalPreference(
            @RequestBody GlobalPreferenceRequestDTO globalPreferenceDTO,
            Authentication authentication
    ){
        globalPreferencesService.addGlobalPreference(globalPreferenceDTO, authentication);
    }

    @GetMapping("/")
    public Page<GlobalPreferenceDTO> getAllGlobalPreferences(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){
        return globalPreferencesService.getAllGlobalPreferences(page, size, authentication);
    }

    @PostMapping("/{id}")
    public void updateGlobalPreference(
            @PathVariable Long id,
            @RequestBody GlobalPreferenceRequestDTO globalPreferenceRequestDTO,
            Authentication authentication
    ){
        try{
            globalPreferencesService.updateGlobalPreference(id, globalPreferenceRequestDTO, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteGlobalPreference(
            @PathVariable Long id,
            Authentication authentication
    ){
        try{
            globalPreferencesService.deleteGlobalPreference(id, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
