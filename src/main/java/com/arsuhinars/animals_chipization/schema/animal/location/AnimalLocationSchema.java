package com.arsuhinars.animals_chipization.schema.animal.location;

import com.arsuhinars.animals_chipization.model.AnimalVisitedLocation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class AnimalLocationSchema {
    @NonNull
    @NotNull
    @Min(1)
    private Long id;

    @NonNull
    @NotNull
    private LocalDateTime dateTimeOfVisitLocationPoint;

    @NonNull
    @NotNull
    @Min(1)
    private Long locationPointId;

    public static AnimalLocationSchema createFromModel(AnimalVisitedLocation location) {
        return new AnimalLocationSchema(
                location.getId(),
                location.getVisitedAt(),
                location.getVisitedLocation().getId()
        );
    }
}
