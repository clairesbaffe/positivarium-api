package com.positivarium.api.repository;

import com.positivarium.api.entity.Mood;
import org.springframework.data.repository.CrudRepository;

public interface MoodRepository extends CrudRepository<Mood, Long> {
}
