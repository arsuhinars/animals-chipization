package com.arsuhinars.animals_chipization.schema.animal_location;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnimalLocationUpdateSchema {
    @NotNull
    @Min(1)
    private Long visitedLocationPointId;

    @NotNull
    @Min(1)
    private Long locationPointId;
}
