package com.arsuhinars.animals_chipization.schema.animal;

import com.arsuhinars.animals_chipization.model.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AnimalSchema {
    @NonNull
    @NotNull
    @Min(1)
    private Long id;

    @NonNull
    @NotNull
    @Size(min = 1)
    private List<@Min(1) Long> animalTypes;

    @NonNull
    @NotNull
    @Min(0)
    private Float weight;

    @NonNull
    @NotNull
    @Min(0)
    private Float length;

    @NonNull
    @NotNull
    @Min(0)
    private Float height;

    @NonNull
    @NotNull
    private AnimalGender gender;

    @NonNull
    @NotNull
    private LifeStatus lifeStatus;

    @NonNull
    @NotNull
    private LocalDateTime chippingDateTime;

    @NonNull
    @NotNull
    @Min(1)
    private Long chipperId;

    @NonNull
    @NotNull
    @Min(1)
    private Long chippingLocationId;

    @NonNull
    @NotNull
    private List<@Min(1) Long> visitedLocations;

    private LocalDateTime deathDateTime;

    public static AnimalSchema createFromModel(Animal animal) {
        var types = animal.getTypes().stream().map(AnimalType::getId).toList();
        var visitedLocations = animal.getVisitedLocations().stream().map(AnimalVisitedLocation::getId).toList();

        var animalSchema = new AnimalSchema(
                animal.getId(),
                types,
                animal.getWeight(),
                animal.getLength(),
                animal.getHeight(),
                animal.getGender(),
                animal.getLifeStatus(),
                animal.getChippingDateTime(),
                animal.getChipper().getId(),
                animal.getChippingLocation().getId(),
                visitedLocations
        );
        animalSchema.setDeathDateTime(animal.getDeathDateTime());

        return animalSchema;
    }
}
