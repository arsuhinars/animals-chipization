package com.arsuhinars.animals_chipization.schema.animal.location;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class AnimalLocationUpdateSchema {
    @NonNull
    @NotNull
    @Min(1)
    private Long visitedLocationPointId;

    @NonNull
    @NotNull
    @Min(1)
    private Long locationPointId;
}
