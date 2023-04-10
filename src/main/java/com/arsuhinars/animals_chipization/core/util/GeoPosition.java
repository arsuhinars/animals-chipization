package com.arsuhinars.animals_chipization.core.util;

import com.arsuhinars.animals_chipization.core.validation.FloatingMax;
import com.arsuhinars.animals_chipization.core.validation.FloatingMin;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GeoPosition {
    @NotNull
    @FloatingMin(value = -90, inclusive = true)
    @FloatingMax(value = 90, inclusive = true)
    @Column(nullable = false)
    private double latitude;

    @NotNull
    @FloatingMin(value = -180, inclusive = true)
    @FloatingMax(value = 180, inclusive = true)
    @Column(nullable = false)
    private double longitude;

    public Vector2d toVector2d() {
        return new Vector2d(latitude, longitude);
    }
}
