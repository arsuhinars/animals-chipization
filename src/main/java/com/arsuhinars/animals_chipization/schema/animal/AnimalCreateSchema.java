package com.arsuhinars.animals_chipization.schema.animal;

import com.arsuhinars.animals_chipization.model.AnimalGender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class AnimalCreateSchema {
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
    @Min(1)
    private Long chipperId;

    @NonNull
    @NotNull
    @Min(1)
    private Long chippingLocationId;
}
