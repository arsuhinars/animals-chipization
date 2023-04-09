package com.arsuhinars.animals_chipization.area.schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AreaCreateSchema {
    @NotBlank
    private String name;

    @NotNull
    @Size(min = 3)
    private List<AreaPointSchema> areaPoints;
}
