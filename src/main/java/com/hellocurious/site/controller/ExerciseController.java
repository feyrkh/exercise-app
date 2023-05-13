package com.hellocurious.site.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hellocurious.site.model.Exercise;
import com.hellocurious.site.model.tracking.DistanceTrackingType;
import com.hellocurious.site.model.tracking.RepTrackingType;
import com.hellocurious.site.model.tracking.TimeTrackingType;
import com.hellocurious.site.model.tracking.WeightTrackingType;
import com.hellocurious.site.service.ExerciseService;

import jakarta.validation.Valid;


@Controller
public class ExerciseController extends BaseController {
    @Autowired
    ExerciseService exerciseService;

    private static final String[] exerciseExamples = new String[] {
        "Some kind of fancy squat!",
        "Lifting heavy stuff!",
        "Punchin' dudes!",
        "Dancing your booty off!",
        "Bend over touch ya knees to ya elbows!",
        "Heavy thinking!",
        "Pulling on ropes or something, I dunno",
        "Walking like an Egyptian!",
        "Pole vaulting!",
        "Freestyle swimming!",
        "Juggling!",
        "Twerking!",
        "Jumping to conclusions!",
        "Jumping rope!",
        "Do the truffle shuffle!",
        "Kickboxing!",
        "Stretching your limits!",
        "Gettin funky with it!",
    };

    @GetMapping("/exercise")
    public String list(@RequestParam(required=false) String sort, @RequestParam(required=false) String filter, Model model) {
        List<Exercise> exercises = exerciseService.getExercises(sort, filter);
        model.addAttribute("exercises", exercises);
        return "exercise/list";
    }

    

    @DeleteMapping("/exercise/{id}") 
    public String deleteTodoItem(@PathVariable("id") String id) { 
        exerciseService.deleteById(id); 

        return "redirect:/exercise"; 
    }



    @GetMapping("/exercise/create")
    public String createExerciseForm(Model model) {
        return editExerciseForm(null, model);
        // var ex = new Exercise();
        // ex.setDifficulty(2);
        // model.addAttribute("exercise", ex);
        // model.addAttribute("exampleName", exerciseExamples[(int)(Math.random()*exerciseExamples.length)]);
        // model.addAttribute("joinedTags", String.join(",", ex.getTags()));
        // return "exercise/edit";
    }

    @PostMapping("/exercise/create")
    public String createExerciseSubmit(@Valid @ModelAttribute Exercise exercise, BindingResult result, @ModelAttribute String joinedTags, Model model) {
        System.out.println("Creating exercise: "+exercise);
        if (result.hasErrors()) {
            System.out.println("Validation errors!");
            return "exercise/create";
        }
        Exercise savedExercise = exerciseService.create(exercise);
        model.addAttribute("exercise", savedExercise);
        System.out.println("Successfully created exercise: "+savedExercise);
        return "exercise/result";
    }


    @GetMapping("/exercise/edit/{id}")
    public String editExerciseForm(@PathVariable(required=false, value="id") String id, Model model) {
        Exercise ex;
        if (id == null) {
            ex = new Exercise();
            ex.setDifficulty(2);
        } else {
            ex = exerciseService.findById(id).orElse(null);
            if (ex == null) {
                return "exercise/list";
            }    
        }
        model.addAttribute("exercise", ex);
        model.addAttribute("exampleName", exerciseExamples[(int)(Math.random()*exerciseExamples.length)]);
        model.addAttribute("joinedTags", String.join(",", ex.getTags()));
        model.addAttribute("DistanceTrackingType", DistanceTrackingType.class);
        model.addAttribute("WeightTrackingType", WeightTrackingType.class);
        model.addAttribute("TimeTrackingType", TimeTrackingType.class);
        model.addAttribute("RepTrackingType", RepTrackingType.class);
        model.addAttribute("showDelete", id != null);
        model.addAttribute("saveMsg", id==null?"Create that exercise!" : "Update that exercise!");
        return "exercise/edit";
    }

    @PostMapping("/exercise/edit/{id}")
    public String editExerciseSubmit(@Valid @ModelAttribute Exercise exercise, @PathVariable("id") String id, BindingResult result, @ModelAttribute String joinedTags, Model model) {
        if ("null".equals(id)) {
            id = null;
        }
        if ("null".equals(exercise.getId())) {
            exercise.setId(null);
        }
        if (id != exercise.getId()) {
            System.out.println("ID mismatch somehow: "+id+" is not equal to "+exercise.getId()+", not updating");
            model.addAttribute("error", "ID mismatch somehow: "+id+" is not equal to "+exercise.getId()+", not updating");
            return editExerciseForm(id, model);
        }
        System.out.println("Updating exercise: "+exercise);
        if (result.hasErrors()) {
            System.out.println("Validation errors!");
            return editExerciseForm(id, model);
        }
        Exercise savedExercise = exerciseService.update(exercise);
        model.addAttribute("exercise", savedExercise);
        System.out.println("Successfully updated exercise: "+savedExercise);
        return list(null, null, model);
    }

    @GetMapping("/exercise/{id}/setEditForm")
    public String setEditFormAjax(@PathVariable("id") String id, Model model) {
        Exercise exercise = exerciseService.findById(id).orElse(null);
        model.addAttribute("exercise", exercise);
        return "exercise/setEditForm";
    }

}
