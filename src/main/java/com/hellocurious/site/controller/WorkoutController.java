package com.hellocurious.site.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hellocurious.site.model.Workout;
import com.hellocurious.site.service.ExerciseService;
import com.hellocurious.site.service.WorkoutService;

import jakarta.validation.Valid;

@Controller
public class WorkoutController extends BaseController {
    @Autowired
    ExerciseService exerciseService;
    @Autowired
    WorkoutService workoutService;

    @GetMapping("/workout")
    public String workoutsList(Model model) {
        model.addAttribute("curWorkout", workoutService.getCurrentWorkout());
        model.addAttribute("prevWorkouts", workoutService.getPreviousWorkouts());
        return "workout/list";
    }

    @GetMapping("/workout/create")
    public String createNewWorkout(Model model) {
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ISO_DATE);
        Workout newWorkout = new Workout();
        newWorkout.setUserId("64566fba0d6715334b69dfb4");
        newWorkout.setName(formattedDate);
        model.addAttribute("workout", newWorkout);
        return "workout/create";
    }

    @GetMapping("/workout/{id}")
    public String viewSpecificWorkout(@PathVariable(required=false, value="id") String id, Model model) {
        Optional<Workout> workout = workoutService.findById(id);
        model.addAttribute("workout", workout.orElse(null));
        if (workout.isEmpty()) {
            return "workout";
        }
        model.addAttribute("taggedExercises", exerciseService.findByTagsIn(workout.get().getTags()));
        List<String> usedTags = workout.get().getTags();
        List<String> unusedTags = exerciseService.getAllUniqueTags()
            .stream().filter((tag) -> !usedTags.contains(tag)).collect(Collectors.toList());
        model.addAttribute("unusedTags", unusedTags);
        return "workout/view";
    }

    @PostMapping("/workout/create")
    public String createNewWorkoutSubmit(
        @Valid @ModelAttribute Workout workout, 
        @RequestParam("submitButton") String submitType, 
        BindingResult result,  
        @ModelAttribute String joinedTags, 
        Model model
    ) {
        Workout curWorkout = workoutService.getCurrentWorkout();
        if (curWorkout != null) {
            return "redirect:/workout/"+curWorkout.getId();
        }
        System.out.println("Submit type: "+submitType);
        workout = workoutService.createNewWorkout(workout);
        return "redirect:/workout/"+workout.getId();
    }

    @PostMapping("workout/{id}/addTag")
    public String addTagToWorkout(
        @PathVariable String id, 
        @ModelAttribute("tagName") String tagName, 
        Model model
    ) {
        Workout workout;
        if (tagName != null && !tagName.isEmpty()) {
            workout = workoutService.addTagToWorkout(id, tagName);
        } else {
            workout = workoutService.findById(id).orElse(null);
        }
        if (workout == null) {
            return "redirect:/workout";
        }
        return "redirect:/workout/"+workout.getId();
    }

    @PostMapping("workout/{id}/addSet")
    public String addSetToWorkout(
            @PathVariable String id,
            @ModelAttribute("exerciseId") String exerciseId,
            @RequestParam("desiredWeight") String desiredWeightStr,
            @RequestParam("desiredDistance") String desiredDistanceStr,
            @RequestParam("desiredTime") String desiredTimeStr,
            @ModelAttribute("desiredReps") Integer desiredReps,
            @ModelAttribute("setCount") Integer setCount
    ) {
        Double desiredWeight = Double.parseDouble(desiredWeightStr);
        Double desiredDistance = Double.parseDouble(desiredDistanceStr);
        Double desiredTime = Double.parseDouble(desiredTimeStr);
    
        workoutService.addSetToWorkout(
                id, exerciseId, desiredWeight, desiredDistance, desiredTime, desiredReps, setCount
        );
    
        return "redirect:/workout/" + id;
    }
        
    

}
