package com.arsuhinars.animals_chipization.animal.schema.animal_type;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalTypeUpdateSchema {
    @NotBlank
    private String type;
}
