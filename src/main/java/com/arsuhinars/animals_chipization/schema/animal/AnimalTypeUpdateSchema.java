package com.arsuhinars.animals_chipization.schema.animal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class AnimalTypeUpdateSchema {
    @NonNull
    @NotNull
    @Min(1)
    private Long oldTypeId;

    @NonNull
    @NotNull
    @Min(1)
    private Long newTypeId;
}
