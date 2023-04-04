package com.arsuhinars.animals_chipization.schema.animal_type;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalTypeCreateSchema {
    @NotBlank
    private String type;
}
