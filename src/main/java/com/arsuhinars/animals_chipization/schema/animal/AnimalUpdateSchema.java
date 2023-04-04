package com.arsuhinars.animals_chipization.schema.animal;

import com.arsuhinars.animals_chipization.enums.Gender;
import com.arsuhinars.animals_chipization.enums.LifeStatus;
import com.arsuhinars.animals_chipization.validation.FloatingMin;
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
public class AnimalUpdateSchema {
    @NotNull
    @FloatingMin(0f)
    private Float weight;

    @NotNull
    @FloatingMin(0f)
    private Float length;

    @NotNull
    @FloatingMin(0f)
    private Float height;

    @NotNull
    private Gender gender;

    @NotNull
    private LifeStatus lifeStatus;

    @NotNull
    @Min(1)
    private Long chipperId;

    @NotNull
    @Min(1)
    private Long chippingLocationId;
}
