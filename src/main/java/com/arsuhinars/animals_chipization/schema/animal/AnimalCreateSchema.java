package com.arsuhinars.animals_chipization.schema.animal;

import com.arsuhinars.animals_chipization.enums.Gender;
import com.arsuhinars.animals_chipization.validation.FloatingMin;
import com.arsuhinars.animals_chipization.validation.UniqueItems;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalCreateSchema {
    @NotNull
    @NotEmpty
    @UniqueItems
    private List<@Min(1) Long> animalTypes;

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
    @Min(1)
    private Long chipperId;

    @NotNull
    @Min(1)
    private Long chippingLocationId;
}
