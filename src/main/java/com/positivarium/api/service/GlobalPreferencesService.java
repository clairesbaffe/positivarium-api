package com.positivarium.api.service;

import com.positivarium.api.dto.GlobalPreferenceDTO;
import com.positivarium.api.dto.GlobalPreferenceRequestDTO;
import com.positivarium.api.entity.GlobalNewsPreference;
import com.positivarium.api.entity.User;
import com.positivarium.api.mapping.GlobalPreferenceMapping;
import com.positivarium.api.repository.GlobalPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GlobalPreferencesService {

    private final GlobalPreferenceMapping globalPreferenceMapping;
    private final GlobalPreferenceRepository globalPreferenceRepository;
    private final UserService userService;

    public void addGlobalPreference(GlobalPreferenceRequestDTO globalPreferenceDTO, Authentication authentication){
        User user = userService.getCurrentUser(authentication);

        GlobalNewsPreference globalNewsPreference = globalPreferenceMapping.dtoToEntity(globalPreferenceDTO, user);
        globalPreferenceRepository.save(globalNewsPreference);
    }

    public Page<GlobalPreferenceDTO> getAllGlobalPreferences(int pageNumber, int pageSize, Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<GlobalNewsPreference> globalNewsPreferences = globalPreferenceRepository.findAllByUserId(user.getId(), pageable);
        return globalNewsPreferences.map(globalPreferenceMapping::entityToDto);
    }

    public void updateGlobalPreference(
            Long id,
            GlobalPreferenceRequestDTO globalPreferenceRequestDTO,
            Authentication authentication
    ) throws Exception {
        User user = userService.getCurrentUser(authentication);

        GlobalNewsPreference globalNewsPreference = globalPreferenceRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new Exception("Global preference not found"));

        globalPreferenceMapping.updateEntityFromDto(globalNewsPreference, globalPreferenceRequestDTO);
        globalPreferenceRepository.save(globalNewsPreference);
    }

    public void deleteGlobalPreference(Long id, Authentication authentication) throws Exception {
        User user = userService.getCurrentUser(authentication);

        GlobalNewsPreference globalNewsPreference = globalPreferenceRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new Exception("Global preference not found"));

        globalPreferenceRepository.delete(globalNewsPreference);
    }
}
