package com.positivarium.api.service;

import com.positivarium.api.dto.PublisherRequestDTO;
import com.positivarium.api.entity.PublisherRequest;
import com.positivarium.api.entity.User;
import com.positivarium.api.enums.PublisherRequestStatusEnum;
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
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return;

        User user = userService.getUser(username);

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

    public PublisherRequestDTO getById(Long id) throws Exception {
        PublisherRequest publisherRequest = publisherRequestRepository.findById(id)
                .orElseThrow(() -> new Exception("Publisher request not found"));
        return publisherRequestMapping.entityToDto(publisherRequest);
    }

    public Page<PublisherRequestDTO> getPublisherRequestsByUser(int pageNumber, int pageSize, Authentication authentication){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return null;

        User user = userService.getUser(username);
        Long userId = user.getId();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PublisherRequest> publisherRequests = publisherRequestRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
        return publisherRequests.map(publisherRequestMapping::entityToDto);
    }

    public void cancelPublisherRequest(Long id, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return;

        User user = userService.getUser(username);
        Long userId = user.getId();

        PublisherRequest publisherRequest = publisherRequestRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new Exception("Publisher request not found"));

        publisherRequest.setStatus(PublisherRequestStatusEnum.CANCELLED);
        publisherRequestRepository.save(publisherRequest);
    }

    public void updatePublisherRequestStatus(Long id, PublisherRequestStatusEnum status) throws Exception {
        if(status == PublisherRequestStatusEnum.PENDING)
            throw new Exception("Publisher request cannot be set to pending");
        if(status == PublisherRequestStatusEnum.CANCELLED)
            throw new Exception("Only user can cancel publisher request");

        PublisherRequest publisherRequest = publisherRequestRepository.findById(id)
                .orElseThrow(() -> new Exception("Publisher request not found"));
        if(publisherRequest.getStatus() == PublisherRequestStatusEnum.APPROVED ||
                publisherRequest.getStatus() == PublisherRequestStatusEnum.REJECTED ||
                publisherRequest.getStatus() == PublisherRequestStatusEnum.CANCELLED)
            throw new Exception("Definitive status cannot be changed");

        publisherRequest.setStatus(status);
        publisherRequestRepository.save(publisherRequest);

        if(status == PublisherRequestStatusEnum.APPROVED){
            User user = publisherRequest.getUser();
            // change user role to publisher
        }
    }
}
