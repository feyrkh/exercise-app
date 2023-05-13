package com.hellocurious.site.repo;

import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.hellocurious.site.model.User;

@CacheConfig(cacheNames = "user") // Specify the cache name
public interface UserRepo extends MongoRepository<User, String> {
    public Optional<User> findById(String id);
    public User findByName(String name);

    
}
