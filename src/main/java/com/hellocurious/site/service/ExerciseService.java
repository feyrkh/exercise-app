package com.hellocurious.site.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hellocurious.site.model.Exercise;
import com.hellocurious.site.repo.ExerciseRepo;

import jakarta.validation.Valid;

@Service
public class ExerciseService {
	@Autowired
    ExerciseRepo exercises;

    public Exercise create(Exercise exercise) {
        Exercise result = exercises.save(exercise);
        return result;
    }

    public List<Exercise> getExercises(String sort, String filter) {
        Sort sortObj = null;
        String[] filterObj = null;
        if (sort != null) {
            sortObj = Sort.by(sort.split(","));
        }
        if (filter != null) {
            filterObj = filter.split(",");
        }
        if (filterObj != null && sortObj != null) {
            return exercises.findByTagsIn(List.of(filterObj), sortObj);
        } else if (filterObj != null && sortObj == null) {
            return exercises.findByTagsIn(List.of(filterObj));
        } else if (filterObj == null && sortObj != null) {
            return exercises.findAll(sortObj);
        } else {
            return exercises.findAll();
        }
    }

    public void deleteById(String id) {
        System.out.println("Deleting by id: "+id);
        exercises.deleteById(id);
    }

    public Optional<Exercise> findById(String id) {
        return exercises.findById(id);
    }

    public Exercise update(@Valid Exercise exercise) {
        return exercises.save(exercise);
    }

    public Object findByTagsIn(List<String> tags) {
        return exercises.findByTagsIn(tags);
    }

    public List<String> getAllUniqueTags() {
        List<Exercise> e = exercises.findAll();
        return e.stream()
                .flatMap(exercise -> exercise.getTags().stream())
                .distinct()
                .collect(Collectors.toList());
    }
}
