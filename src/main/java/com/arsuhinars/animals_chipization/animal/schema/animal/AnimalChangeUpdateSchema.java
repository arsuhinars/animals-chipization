package com.arsuhinars.animals_chipization.animal.schema.animal;

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
public class AnimalChangeUpdateSchema {
    @NotNull
    @Min(1)
    private Long oldTypeId;

    @NotNull
    @Min(1)
    private Long newTypeId;
}
