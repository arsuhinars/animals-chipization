package com.arsuhinars.animals_chipization.schema.animal.location;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class AnimalLocationCreateSchema {
    @NonNull
    @NotNull
    @Min(1)
    private Long animalId;

    @NonNull
    @NotNull
    @Min(1)
    private Long pointId;
}
