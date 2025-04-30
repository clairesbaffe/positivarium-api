package com.positivarium.api.mapping;

import com.positivarium.api.dto.CategoryDTO;
import com.positivarium.api.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapping {

    public CategoryDTO entityToDto(Category category){
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .generalCategory(category.getGeneralCategory().getName())
                .build();
    }
}
