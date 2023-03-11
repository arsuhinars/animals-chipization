package com.arsuhinars.animals_chipization.schema.animal.location;

import com.arsuhinars.animals_chipization.model.AnimalVisitedLocation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class AnimalLocationSchema {
    @NotNull
    @Min(1)
    private Long id;

    @NotNull
    private OffsetDateTime dateTimeOfVisitLocationPoint;

    @NotNull
    @Min(1)
    private Long locationPointId;

    public AnimalLocationSchema(Long id, OffsetDateTime dateTimeOfVisitLocationPoint, Long locationPointId) {
        this.id = id;
        this.dateTimeOfVisitLocationPoint = dateTimeOfVisitLocationPoint;
        this.locationPointId = locationPointId;
    }

    public static AnimalLocationSchema createFromModel(AnimalVisitedLocation location) {
        return new AnimalLocationSchema(
                location.getId(),
                location.getVisitedAt(),
                location.getVisitedLocation().getId()
        );
    }
}
