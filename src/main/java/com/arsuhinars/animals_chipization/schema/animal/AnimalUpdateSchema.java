package com.arsuhinars.animals_chipization.schema.animal;

import com.arsuhinars.animals_chipization.model.AnimalGender;
import com.arsuhinars.animals_chipization.model.LifeStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class AnimalUpdateSchema {
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
    @Min(1)
    private Long chipperId;

    @NonNull
    @NotNull
    @Min(1)
    private Long chippingLocationId;
}
