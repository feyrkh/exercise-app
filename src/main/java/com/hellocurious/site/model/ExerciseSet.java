package com.hellocurious.site.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class ExerciseSet {
    @Id
    private String id;
    private String exerciseId;
    private String workoutId;
    private List<Double> desiredWeight;
    private List<Double> actualWeight;
    private List<Double> desiredDistance;
    private List<Double> actualDistance;
    private List<Double> desiredTime;
    private List<Double> actualTime;
    private List<Integer> desiredReps;
    private List<Integer> actualReps; 
}
