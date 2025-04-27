package com.positivarium.api.mapping;

import com.positivarium.api.dto.SimpleArticleDTO;
import com.positivarium.api.entity.Article;
import org.springframework.stereotype.Component;

@Component
public class SimpleArticleMapping {

    public SimpleArticleDTO entityToDto(Article article){
        return SimpleArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .main_image(article.getMainImage())
                .build();
    }
}
