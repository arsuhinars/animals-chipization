package com.arsuhinars.animals_chipization.area.model;

import com.arsuhinars.animals_chipization.area.schema.AreaPointSchema;
import com.arsuhinars.animals_chipization.area.util.Vector2d;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AreaPoint {
    @Column(nullable = false)
    private double longitude;
    @Column(nullable = false)
    private double latitude;

    public AreaPoint(AreaPointSchema schema) {
        this.latitude = schema.getLatitude();
        this.longitude = schema.getLongitude();
    }

    public Vector2d toVector2d() {
        return new Vector2d(latitude, longitude);
    }
}
