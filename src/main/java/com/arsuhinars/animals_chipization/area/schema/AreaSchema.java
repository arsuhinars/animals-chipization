package com.arsuhinars.animals_chipization.area.schema;

import com.arsuhinars.animals_chipization.area.model.Area;
import com.arsuhinars.animals_chipization.core.util.GeoPosition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AreaSchema {
    @NotNull
    @Min(1)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Size(min = 3)
    private List<GeoPosition> areaPoints;

    public AreaSchema(Area area) {
        this.id = area.getId();
        this.name = area.getName();
        this.areaPoints = area.getPoints();
    }
}
