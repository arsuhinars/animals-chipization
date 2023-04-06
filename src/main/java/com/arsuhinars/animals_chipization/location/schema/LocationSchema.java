package com.arsuhinars.animals_chipization.location.schema;

import com.arsuhinars.animals_chipization.location.model.Location;
import com.arsuhinars.animals_chipization.core.validation.FloatingMax;
import com.arsuhinars.animals_chipization.core.validation.FloatingMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
