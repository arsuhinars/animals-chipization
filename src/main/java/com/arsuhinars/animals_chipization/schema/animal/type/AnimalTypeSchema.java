package com.arsuhinars.animals_chipization.schema.animal.type;

import com.arsuhinars.animals_chipization.model.AnimalType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class AnimalTypeSchema {
    private Long id;

    @NonNull
    @NotBlank
    private String type;

    public static AnimalTypeSchema createFromModel(AnimalType animalType) {
        var animalTypeSchema = new AnimalTypeSchema(
                animalType.getType()
        );
        animalTypeSchema.setId(animalType.getId());
        return animalTypeSchema;
    }
}
