package com.arsuhinars.animals_chipization.schema.animal;

import com.arsuhinars.animals_chipization.model.AnimalGender;
import com.arsuhinars.animals_chipization.model.LifeStatus;
import com.arsuhinars.animals_chipization.validation.FloatingMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalUpdateSchema {
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
    private LifeStatus lifeStatus;

    @NotNull
    @Min(1)
    private Long chipperId;

    @NotNull
    @Min(1)
    private Long chippingLocationId;

    public AnimalUpdateSchema(
        Float weight, Float length, Float height,
        AnimalGender gender,
        LifeStatus lifeStatus,
        Long chipperId,
        Long chippingLocationId
    ) {
        this.weight = weight;
        this.length = length;
        this.height = height;
        this.gender = gender;
        this.lifeStatus = lifeStatus;
        this.chipperId = chipperId;
        this.chippingLocationId = chippingLocationId;
    }
}
