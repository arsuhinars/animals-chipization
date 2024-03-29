package com.arsuhinars.animals_chipization.animal.schema.animal_location;

import com.arsuhinars.animals_chipization.animal.model.AnimalLocation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalLocationSchema {
    @NotNull
    @Min(1)
    private Long id;

    @NotNull
    private OffsetDateTime dateTimeOfVisitLocationPoint;

    @NotNull
    @Min(1)
    private Long locationPointId;

    public AnimalLocationSchema(AnimalLocation location) {
        this.id = location.getId();
        this.dateTimeOfVisitLocationPoint = location.getVisitedAt();
        this.locationPointId = location.getVisitedLocation().getId();
    }
}
