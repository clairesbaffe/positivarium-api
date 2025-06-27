package com.positivarium.api.repository;

import com.positivarium.api.entity.HttpLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HttpLogRepository extends JpaRepository<HttpLog, Long> {
}
