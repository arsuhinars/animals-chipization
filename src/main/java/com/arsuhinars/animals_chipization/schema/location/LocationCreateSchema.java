package com.arsuhinars.animals_chipization.schema.location;

import com.arsuhinars.animals_chipization.validation.FloatingMax;
import com.arsuhinars.animals_chipization.validation.FloatingMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreateSchema {
    @NotNull
    @FloatingMin(value = -90, inclusive = true)
    @FloatingMax(value = 90, inclusive = true)
    private Double latitude;

    @NotNull
    @FloatingMin(value = -180, inclusive = true)
    @FloatingMax(value = 180, inclusive = true)
    private Double longitude;
}
