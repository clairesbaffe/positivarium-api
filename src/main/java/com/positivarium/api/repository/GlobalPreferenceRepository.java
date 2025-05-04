package com.positivarium.api.repository;

import com.positivarium.api.entity.GlobalNewsPreference;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GlobalPreferenceRepository extends CrudRepository<GlobalNewsPreference, Long> {

    Page<GlobalNewsPreference> findAllByUserId(Long userId, Pageable pageable);

    Optional<GlobalNewsPreference> findByIdAndUserId(Long id, Long userId);

    @Transactional
    void deleteByIdAndUserId(Long id, Long userId);
}
