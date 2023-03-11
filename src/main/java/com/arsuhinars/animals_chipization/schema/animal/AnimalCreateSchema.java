package com.arsuhinars.animals_chipization.schema.animal;

import com.arsuhinars.animals_chipization.model.AnimalGender;
import com.arsuhinars.animals_chipization.validation.FloatingMin;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnimalCreateSchema {
    @NotNull
    @NotEmpty
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
    private AnimalGender gender;

    @NotNull
    @Min(1)
    private Long chipperId;

    @NotNull
    @Min(1)
    private Long chippingLocationId;

    public AnimalCreateSchema(
        List<Long> animalTypes,
        Float weight, Float length, Float height,
        AnimalGender gender,
        Long chipperId,
        Long chippingLocationId
    ) {
        this.animalTypes = animalTypes;
        this.weight = weight;
        this.length = length;
        this.height = height;
        this.gender = gender;
        this.chipperId = chipperId;
        this.chippingLocationId = chippingLocationId;
    }
}
