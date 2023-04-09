package com.arsuhinars.animals_chipization.area.schema;

import com.arsuhinars.animals_chipization.area.model.AreaPoint;
import com.arsuhinars.animals_chipization.area.util.Vector2d;
import com.arsuhinars.animals_chipization.core.validation.FloatingMax;
import com.arsuhinars.animals_chipization.core.validation.FloatingMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AreaPointSchema {
    @FloatingMin(value = -180, inclusive = true)
    @FloatingMax(value = 180, inclusive = true)
    private double longitude;

    @FloatingMin(value = -90, inclusive = true)
    @FloatingMax(value = 90, inclusive = true)
    private double latitude;

    public AreaPointSchema(AreaPoint point) {
        this.longitude = point.getLongitude();
        this.latitude = point.getLatitude();
    }

    public Vector2d toVector2d() {
        return new Vector2d(latitude, longitude);
    }
}
