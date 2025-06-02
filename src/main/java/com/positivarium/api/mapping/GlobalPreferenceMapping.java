package com.positivarium.api.mapping;

import com.positivarium.api.dto.CategoryDTO;
import com.positivarium.api.dto.GlobalPreferenceDTO;
import com.positivarium.api.dto.GlobalPreferenceRequestDTO;
import com.positivarium.api.entity.Category;
import com.positivarium.api.entity.GlobalNewsPreference;
import com.positivarium.api.entity.Mood;
import com.positivarium.api.entity.User;
import com.positivarium.api.repository.CategoryRepository;
import com.positivarium.api.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class GlobalPreferenceMapping {

    private final MoodRepository moodRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapping categoryMapping;
    private final MoodMapping moodMapping;

    public GlobalNewsPreference dtoToEntity(GlobalPreferenceRequestDTO globalPreferenceRequestDTO, User user){
        Mood mood = (globalPreferenceRequestDTO.moodId() != null) ?
                moodRepository.findById(globalPreferenceRequestDTO.moodId())
                        .orElseThrow(() -> new RuntimeException("Mood not found")) : null;

        Iterable<Category> iterableCategories = categoryRepository.findAllById(globalPreferenceRequestDTO.categoryIds());
        Set<Category> categories = StreamSupport
                .stream(iterableCategories.spliterator(), false)
                .collect(Collectors.toSet());

        return GlobalNewsPreference.builder()
                .mood(mood)
                .categories(categories)
                .user(user)
                .build();
    }

    public GlobalPreferenceDTO entityToDto(GlobalNewsPreference globalNewsPreference){
        Set<CategoryDTO> categoryDTOs = globalNewsPreference.getCategories().stream()
                .map(categoryMapping::entityToDto)
                .collect(Collectors.toSet());

        return GlobalPreferenceDTO.builder()
                .id(globalNewsPreference.getId())
                .mood(globalNewsPreference.getMood() != null ? moodMapping.entityToDto(globalNewsPreference.getMood()) : null)
                .categories(categoryDTOs)
                .build();
    }

    public void updateEntityFromDto(GlobalNewsPreference globalNewsPreference, GlobalPreferenceRequestDTO globalPreferenceRequestDTO){
        Mood mood = (globalPreferenceRequestDTO.moodId() != null) ?
                moodRepository.findById(globalPreferenceRequestDTO.moodId())
                        .orElseThrow(() -> new RuntimeException("Mood not found")) : null;

        Iterable<Category> iterableCategories = categoryRepository.findAllById(globalPreferenceRequestDTO.categoryIds());
        Set<Category> categories = StreamSupport
                .stream(iterableCategories.spliterator(), false)
                .collect(Collectors.toSet());

        globalNewsPreference.setMood(mood);
        globalNewsPreference.setCategories(categories);
    }
}
