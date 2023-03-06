package com.arsuhinars.animals_chipization.schema;

import com.arsuhinars.animals_chipization.model.Location;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class LocationSchema {
    private Long id;

    @NonNull
    @NotNull
    @Min(-90)
    @Max(90)
    private Double latitude;

    @NonNull
    @NotNull
    @Min(-180)
    @Max(180)
    private Double longitude;

    public static LocationSchema createFromModel(Location location) {
        var locationSchema = new LocationSchema(
                location.getLatitude(),
                location.getLongitude()
        );
        locationSchema.setId(locationSchema.getId());
        return locationSchema;
    }
}
