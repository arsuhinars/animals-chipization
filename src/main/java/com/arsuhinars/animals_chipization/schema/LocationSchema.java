package com.arsuhinars.animals_chipization.schema;

import com.arsuhinars.animals_chipization.model.Location;
import com.arsuhinars.animals_chipization.validation.FloatingMax;
import com.arsuhinars.animals_chipization.validation.FloatingMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LocationSchema {
    @NotNull
    @Min(1)
    private Long id;

    @NotNull
    @FloatingMin(value = -90, inclusive = true)
    @FloatingMax(value = 90, inclusive = true)
    private Double latitude;

    @NotNull
    @FloatingMin(value = -180, inclusive = true)
    @FloatingMax(value = 180, inclusive = true)
    private Double longitude;

    public LocationSchema(Location location) {
        this.id = location.getId();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}
