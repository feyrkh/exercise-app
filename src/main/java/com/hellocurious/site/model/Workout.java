package com.hellocurious.site.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hellocurious.site.repo.ExerciseRepo;
import com.hellocurious.site.repo.ExerciseSetRepo;

import lombok.Data;

@Document
@Data
public class Workout {
    @Id
    private String id;
    private String userId;
    private String name;
    private Date completionDate;
    private List<String> exerciseSetIds;
    private List<String> tags = new ArrayList<>();

    @Transient
    @Autowired
    private ExerciseRepo exerciseRepository;

    @Transient
    @Autowired
    private ExerciseSetRepo exerciseSetRepository;

    public List<String> getExerciseNames() {
        Set<String> exerciseNames = new HashSet<>();
        if (exerciseSetIds == null) {
            return new ArrayList<>();
        }
        for (String exerciseSetId : exerciseSetIds) {
            Exercise exercise = exerciseRepository.findById(exerciseSetId).orElse(null);
            if (exercise != null) {
                exerciseNames.add(exercise.getName());
            }
        }
        return exerciseNames.stream().sorted().toList();
    }

    public List<ExerciseSet> getExerciseSets() {
        return exerciseSetRepository.findByWorkoutId(id);
    }

    public String getJoinedTags() {
        return String.join(",", tags.stream().sorted().toList());
    }
    
    public void setJoinedTags(String csvTags) {
        tags = List.of(csvTags.split(",")).stream().filter(Predicate.not(String::isBlank)).map(String::toLowerCase).collect(Collectors.toList());
    }
}
