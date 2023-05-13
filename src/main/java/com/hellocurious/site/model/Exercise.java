package com.hellocurious.site.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hellocurious.site.model.tracking.DistanceTrackingType;
import com.hellocurious.site.model.tracking.RepTrackingType;
import com.hellocurious.site.model.tracking.TimeTrackingType;
import com.hellocurious.site.model.tracking.WeightTrackingType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Document
@Data
public class Exercise {
    @Id
    private String id;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 250)
    private String name;
    @Max(3)
    @Min(1)
    private int difficulty;
    private List<String> tags = new ArrayList<>();
    private String description;
    private WeightTrackingType weightTrackType = WeightTrackingType.NONE;
    private List<List<Integer>> weightHistory = null;
    private DistanceTrackingType distanceTrackType = DistanceTrackingType.NONE;
    private List<List<Double>> distanceHistory = null;
    private TimeTrackingType timeTrackType = TimeTrackingType.NONE;
    private List<List<Double>> timeHistory = null;
    private RepTrackingType repTrackType = RepTrackingType.NONE;
    private List<List<Double>> repHistory = null;

    public String getJoinedTags() {
        return String.join(",", tags.stream().sorted().toList());
    }
    
    public void setJoinedTags(String csvTags) {
        tags = List.of(csvTags.split(",")).stream().filter(Predicate.not(String::isBlank)).map(String::toLowerCase).collect(Collectors.toList());
    }
}
