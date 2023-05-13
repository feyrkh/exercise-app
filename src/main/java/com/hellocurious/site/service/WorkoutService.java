package com.hellocurious.site.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hellocurious.site.model.Exercise;
import com.hellocurious.site.model.ExerciseSet;
import com.hellocurious.site.model.User;
import com.hellocurious.site.model.Workout;
import com.hellocurious.site.repo.UserRepo;
import com.hellocurious.site.repo.WorkoutRepo;

import jakarta.validation.Valid;

@Service
public class WorkoutService {
	@Autowired
    WorkoutRepo workouts;
	@Autowired
    UserRepo users;
    @Autowired ExerciseService exerciseService;
    
    public Workout getCurrentWorkout() {
        Optional<User> curUser = users.findById("64566fba0d6715334b69dfb4");
        if (curUser.isEmpty()) {
            return null;
        }
        String curWorkoutId = curUser.get().getCurWorkoutId();
        if (curWorkoutId == null) {
            return null;
        }
        Optional<Workout> curWorkout = workouts.findById(curWorkoutId);
        return curWorkout.orElse(null);
    }

    public Workout createNewWorkout(Workout workout) {
        Optional<User> curUser = users.findById(workout.getUserId());
        if (curUser.isEmpty()) {
            return null;
        }
        if (curUser.get().getCurWorkoutId() != null) {
            return workouts.findById(curUser.get().getCurWorkoutId()).orElse(null);
        }
        workout = workouts.save(workout);
        curUser.get().setCurWorkoutId(workout.getId());
        users.save(curUser.get());
        return workout;
    }

    public List<Workout> getPreviousWorkouts() {
        return workouts.findTop10ByUserIdOrderByCompletionDateDesc("64566fba0d6715334b69dfb4");
    }

    public Optional<Workout> findById(String id) {
        return workouts.findById(id);
    }

    public @Valid Workout update(@Valid Workout workout) {
        return workouts.save(workout);
    }

    public Workout addTagToWorkout(String id, String tagName) {
        Workout workout = workouts.findById(id).orElse(null);
        if (workout == null) {
            return null;
        }
        List<String> tags = workout.getTags();
        if (!tags.contains(tagName)) {
            tags.add(tagName);
            workout.setTags(tags);
            workout = workouts.save(workout);
        }
        return workout;
    }

    public Workout addSetToWorkout(
        String id, String exerciseId, Double desiredWeight, Double desiredDistance,
        Double desiredTime, Integer desiredReps, Integer setCount
    ) {
        Workout workout;
        workout = workouts.findById(id).orElse(null);
        if (workout == null) {
            return null;
        }
        Exercise exercise = exerciseService.findById(exerciseId).orElse(null);
        if (exercise != null) {
            if (setCount == null || setCount < 1) {
                setCount = 4;
            } else if (setCount > 10) {
                setCount = 10;
            }
            ExerciseSet set = new ExerciseSet();
            set.setExerciseId(exerciseId);
            for (int i=0; i<setCount;i++) {
                if (desiredDistance != null) {
                    set.getDesiredDistance().add(desiredDistance);
                    set.getActualDistance().add(0d);
                }
                if (desiredWeight != null) {
                    set.getDesiredWeight().add(desiredWeight);
                    set.getActualWeight().add(0d);
                }
                if (desiredTime != null) {
                    set.getDesiredTime().add(desiredTime);
                    set.getActualTime().add(0d);
                }
                if (desiredReps != null) {
                    set.getDesiredReps().add(desiredReps);
                    set.getActualReps().add(0);
                }
            }
            workout.getExerciseSets().add(set);
        }
        return workout;
    }
}
