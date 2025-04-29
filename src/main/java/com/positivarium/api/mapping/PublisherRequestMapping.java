package com.positivarium.api.mapping;

import com.positivarium.api.dto.PublisherRequestDTO;
import com.positivarium.api.entity.PublisherRequest;
import com.positivarium.api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PublisherRequestMapping {

    public PublisherRequestDTO entityToDto(PublisherRequest publisherRequest){
        return PublisherRequestDTO.builder()
                .id(publisherRequest.getId())
                .motivation(publisherRequest.getMotivation())
                .status(publisherRequest.getStatus())
                .username(publisherRequest.getUser().getUsername())
                .createdAt(publisherRequest.getCreatedAt())
                .updatedAt(publisherRequest.getUpdatedAt())
                .build();
    }

    public PublisherRequest dtoToEntity(PublisherRequestDTO publisherRequestDTO, User user){
        return PublisherRequest.builder()
                .motivation(publisherRequestDTO.motivation())
                .status(publisherRequestDTO.status())
                .user(user)
                .build();
    }
}
