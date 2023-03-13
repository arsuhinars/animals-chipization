package com.arsuhinars.animals_chipization.schema.animal.type;

import com.arsuhinars.animals_chipization.model.AnimalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalTypeSchema {
    private Long id;

    @NotBlank
    private String type;

    public AnimalTypeSchema(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public static AnimalTypeSchema createFromModel(AnimalType animalType) {
        return new AnimalTypeSchema(
            animalType.getId(),
            animalType.getType()
        );
    }
}
