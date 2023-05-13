package com.hellocurious.site.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.hellocurious.site.model.ExerciseSet;

@CacheConfig(cacheNames = "exerciseSets")
public interface ExerciseSetRepo extends MongoRepository<ExerciseSet, String> {
    @Cacheable
    Optional<ExerciseSet> findById(String id);
    
    @Cacheable
    List<ExerciseSet> findByWorkoutId(String workoutId);
}