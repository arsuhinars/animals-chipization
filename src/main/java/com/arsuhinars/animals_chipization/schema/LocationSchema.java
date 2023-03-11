package com.arsuhinars.animals_chipization.schema;

import com.arsuhinars.animals_chipization.model.Location;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationSchema {
    private Long id;

    @NotNull
    @Min(-90)
    @Max(90)
    private Double latitude;

    @NotNull
    @Min(-180)
    @Max(180)
    private Double longitude;

    public LocationSchema(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationSchema createFromModel(Location location) {
        return new LocationSchema(
            location.getId(),
            location.getLatitude(),
            location.getLongitude()
        );
    }
}
