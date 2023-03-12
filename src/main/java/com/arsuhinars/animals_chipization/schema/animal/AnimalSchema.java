package com.arsuhinars.animals_chipization.schema.animal;

import com.arsuhinars.animals_chipization.enums.Gender;
import com.arsuhinars.animals_chipization.enums.LifeStatus;
import com.arsuhinars.animals_chipization.model.*;
import com.arsuhinars.animals_chipization.validation.FloatingMin;
import com.arsuhinars.animals_chipization.validation.Unique;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class AnimalSchema {
    @NotNull
    @Min(1)
    private Long id;

    @NotNull
    @NotEmpty
    @Unique
    private List<@Min(1) Long> animalTypes;

    @NotNull
    @FloatingMin(0f)
    private Float weight;

    @NotNull
    @FloatingMin(0f)
    private Float length;

    @NotNull
    @FloatingMin(0f)
    private Float height;

    @NotNull
    private Gender gender;

    @NotNull
    private LifeStatus lifeStatus;

    @NotNull
    private OffsetDateTime chippingDateTime;

    @NotNull
    @Min(1)
    private Long chipperId;

    @NotNull
    @Min(1)
    private Long chippingLocationId;

    @NotNull
    private List<@Min(1) Long> visitedLocations;

    private OffsetDateTime deathDateTime;

    public AnimalSchema(
        Long id,
        List<Long> animalTypes,
        Float weight, Float length, Float height,
        Gender gender,
        LifeStatus lifeStatus,
        OffsetDateTime chippingDateTime,
        Long chipperId,
        Long chippingLocationId,
        List<Long> visitedLocations,
        OffsetDateTime deathDateTime
    ) {
        this.id = id;
        this.animalTypes = animalTypes;
        this.weight = weight;
        this.length = length;
        this.height = height;
        this.gender = gender;
        this.lifeStatus = lifeStatus;
        this.chippingDateTime = chippingDateTime;
        this.chipperId = chipperId;
        this.chippingLocationId = chippingLocationId;
        this.visitedLocations = visitedLocations;
        this.deathDateTime = deathDateTime;
    }

    public static AnimalSchema createFromModel(Animal animal) {
        var types = animal.getTypes().stream().map(AnimalType::getId).toList();
        var visitedLocations = animal.getVisitedLocations().stream().map(AnimalVisitedLocation::getId).toList();

        return new AnimalSchema(
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
            visitedLocations,
            animal.getDeathDateTime()
        );
    }
}
