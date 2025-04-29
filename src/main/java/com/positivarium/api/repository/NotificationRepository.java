package com.positivarium.api.repository;

import com.positivarium.api.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    Page<Notification> findAllBySenderId(Long userId, Pageable pageable);

    Page<Notification> findAllByReceiverId(Long userId, Pageable pageable);

    Page<Notification> findAllByReceiverIdAndIsReadFalse(Long userId, Pageable pageable);

    Optional<Notification> findByIdAndReceiverIdAndIsReadFalse(Long id, Long userId);
}
