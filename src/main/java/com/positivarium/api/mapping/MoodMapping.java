package com.positivarium.api.mapping;

import com.positivarium.api.dto.MoodDTO;
import com.positivarium.api.entity.Mood;
import org.springframework.stereotype.Component;

@Component
public class MoodMapping {

    public MoodDTO entityToDto(Mood mood){
        return MoodDTO.builder()
                .id(mood.getId())
                .name(mood.getName())
                .type(mood.getType())
                .build();
    }
}
