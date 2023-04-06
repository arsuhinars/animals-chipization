package com.arsuhinars.animals_chipization.animal.schema.animal_type;

import com.arsuhinars.animals_chipization.animal.model.AnimalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalTypeSchema {
    @NotNull
    @Min(1)
    private Long id;

    @NotBlank
    private String type;

    public AnimalTypeSchema(AnimalType animalType) {
        this.id = animalType.getId();
        this.type = animalType.getType();
    }
}
