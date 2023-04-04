package com.arsuhinars.animals_chipization.schema.animal;

import com.arsuhinars.animals_chipization.enums.Gender;
import com.arsuhinars.animals_chipization.enums.LifeStatus;
import com.arsuhinars.animals_chipization.model.*;
import com.arsuhinars.animals_chipization.validation.FloatingMin;
import com.arsuhinars.animals_chipization.validation.UniqueItems;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalSchema {
    @NotNull
    @Min(1)
    private Long id;

    @NotNull
    @NotEmpty
    @UniqueItems
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

    public AnimalSchema(Animal animal) {
        this.id = animal.getId();
        this.animalTypes = animal.getTypes().stream().map(AnimalType::getId).toList();
        this.weight = animal.getWeight();
        this.length = animal.getLength();
        this.height = animal.getHeight();
        this.gender = animal.getGender();
        this.lifeStatus = animal.getLifeStatus();
        this.chippingDateTime = animal.getChippingDateTime();
        this.chipperId = animal.getChipper().getId();
        this.chippingLocationId = animal.getChippingLocation().getId();
        this.visitedLocations = animal.getVisitedLocations().stream().map(AnimalLocation::getId).toList();
        this.deathDateTime = animal.getDeathDateTime();
    }
}
