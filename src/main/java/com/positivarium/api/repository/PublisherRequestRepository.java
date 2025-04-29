package com.positivarium.api.repository;

import com.positivarium.api.entity.PublisherRequest;
import com.positivarium.api.enums.PublisherRequestStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PublisherRequestRepository  extends CrudRepository<PublisherRequest, Long> {

    Page<PublisherRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<PublisherRequest> findAllByStatusInOrderByCreatedAtDesc(List<PublisherRequestStatusEnum> statusEnums, Pageable pageable);

    Optional<PublisherRequest> findByUserIdAndId(Long userId, Long id);

    Page<PublisherRequest> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
