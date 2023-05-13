package com.hellocurious.site.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.hellocurious.site.model.Exercise;

@CacheConfig(cacheNames = "exercises")
public interface ExerciseRepo extends MongoRepository<Exercise, String> {
    
    @Cacheable
    @Query("{name:'?0'}")
    Exercise findItemByName(String name);

    @Cacheable
    Optional<Exercise> findById(String id);
    
    @Cacheable
    List<Exercise> findByTagsIn(List<String> tags);
    @Cacheable
    List<Exercise> findByTagsIn(List<String> tags, Sort sort);
    
    public long count();
}