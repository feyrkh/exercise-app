package com.hellocurious.site.repo;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.hellocurious.site.model.Workout;

@CacheConfig(cacheNames = "workouts") // Specify the cache name
public interface WorkoutRepo extends MongoRepository<Workout, String> {
    public Workout findByUserId(String userId);
    public List<Workout> findTop10ByUserIdOrderByCompletionDateDesc(String userId);
    public List<Workout> findAllByUserIdOrderByCompletionDateDesc(String userId);
}
