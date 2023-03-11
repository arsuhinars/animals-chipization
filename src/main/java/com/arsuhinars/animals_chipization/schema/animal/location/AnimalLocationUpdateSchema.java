package com.arsuhinars.animals_chipization.schema.animal.location;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalLocationUpdateSchema {
    @NotNull
    @Min(1)
    private Long visitedLocationPointId;

    @NotNull
    @Min(1)
    private Long locationPointId;

    public AnimalLocationUpdateSchema(Long visitedLocationPointId, Long locationPointId) {
        this.visitedLocationPointId = visitedLocationPointId;
        this.locationPointId = locationPointId;
    }
}
