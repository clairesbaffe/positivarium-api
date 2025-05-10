package com.positivarium.api.service;

import com.positivarium.api.dto.PublisherRequestDTO;
import com.positivarium.api.entity.PublisherRequest;
import com.positivarium.api.entity.User;
import com.positivarium.api.enums.PublisherRequestStatusEnum;
import com.positivarium.api.exception.InvalidStatusTransitionException;
import com.positivarium.api.exception.ResourceNotFoundException;
import com.positivarium.api.mapping.PublisherRequestMapping;
import com.positivarium.api.repository.PublisherRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherRequestService {

    private final PublisherRequestMapping publisherRequestMapping;
    private final PublisherRequestRepository publisherRequestRepository;
    private final UserService userService;

    public void createPublisherRequest(
            PublisherRequestDTO publisherRequestDTO,
            Authentication authentication
    ){
        User user = userService.getCurrentUser(authentication);

        PublisherRequest publisherRequest = publisherRequestMapping.dtoToEntity(publisherRequestDTO, user);
        publisherRequest.setStatus(PublisherRequestStatusEnum.PENDING);
        publisherRequestRepository.save(publisherRequest);
    }

    public Page<PublisherRequestDTO> getPublisherRequests(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PublisherRequest> publisherRequests = publisherRequestRepository.findAllByOrderByCreatedAtDesc(pageable);
        return publisherRequests.map(publisherRequestMapping::entityToDto);
    }

    public Page<PublisherRequestDTO> getActivePublisherRequests(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<PublisherRequestStatusEnum> statusList = List.of(PublisherRequestStatusEnum.PENDING, PublisherRequestStatusEnum.UNDER_REVIEW);
        Page<PublisherRequest> publisherRequests = publisherRequestRepository.findAllByStatusInOrderByCreatedAtDesc(statusList, pageable);
        return publisherRequests.map(publisherRequestMapping::entityToDto);
    }

    public PublisherRequestDTO getById(Long id){
        PublisherRequest publisherRequest = publisherRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher request not found"));
        return publisherRequestMapping.entityToDto(publisherRequest);
    }

    public Page<PublisherRequestDTO> getPublisherRequestsByUser(int pageNumber, int pageSize, Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<PublisherRequest> publisherRequests = publisherRequestRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId(), pageable);
        return publisherRequests.map(publisherRequestMapping::entityToDto);
    }

    public void cancelPublisherRequest(Long id, Authentication authentication){
        User user = userService.getCurrentUser(authentication);

        PublisherRequest publisherRequest = publisherRequestRepository.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher request not found"));

        publisherRequest.setStatus(PublisherRequestStatusEnum.CANCELLED);
        publisherRequestRepository.save(publisherRequest);
    }

    public void updatePublisherRequestStatus(Long id, PublisherRequestStatusEnum status){
        if(status == PublisherRequestStatusEnum.PENDING)
            throw new InvalidStatusTransitionException("Publisher request cannot be set to pending");
        if(status == PublisherRequestStatusEnum.CANCELLED)
            throw new InvalidStatusTransitionException("Only user can cancel publisher request");

        PublisherRequest publisherRequest = publisherRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher request not found"));
        if(publisherRequest.getStatus() == PublisherRequestStatusEnum.APPROVED ||
                publisherRequest.getStatus() == PublisherRequestStatusEnum.REJECTED ||
                publisherRequest.getStatus() == PublisherRequestStatusEnum.CANCELLED)
            throw new InvalidStatusTransitionException("Definitive status cannot be changed");

        publisherRequest.setStatus(status);
        publisherRequestRepository.save(publisherRequest);

        if(status == PublisherRequestStatusEnum.APPROVED){
            User user = publisherRequest.getUser();

            userService.grantPublisher(user.getUsername());
        }
    }
}
