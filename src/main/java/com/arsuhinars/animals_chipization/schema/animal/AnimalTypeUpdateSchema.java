package com.arsuhinars.animals_chipization.schema.animal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalTypeUpdateSchema {
    @NotNull
    @Min(1)
    private Long oldTypeId;

    @NotNull
    @Min(1)
    private Long newTypeId;

    public AnimalTypeUpdateSchema(Long oldTypeId, Long newTypeId) {
        this.oldTypeId = oldTypeId;
        this.newTypeId = newTypeId;
    }
}
