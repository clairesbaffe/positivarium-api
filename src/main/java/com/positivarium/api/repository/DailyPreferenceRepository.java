package com.positivarium.api.repository;

import com.positivarium.api.entity.DailyNewsPreference;
import org.springframework.data.repository.CrudRepository;

public interface DailyPreferenceRepository extends CrudRepository<DailyNewsPreference, Long> {
}
