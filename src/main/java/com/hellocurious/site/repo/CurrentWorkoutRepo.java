package com.hellocurious.site.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hellocurious.site.model.Workout;

public interface CurrentWorkoutRepo extends MongoRepository<Workout, String> {
    
}
