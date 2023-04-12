package com.arsuhinars.animals_chipization.area.schema;

import com.arsuhinars.animals_chipization.animal.model.AnimalType;
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
public class AnimalsAnalytics {
    @NotNull
    private String animalType;

    @NotNull
    @Min(1)
    private Long animalTypeId;

    @NotNull
    @Min(0)
    private Long quantityAnimals;

    @NotNull
    @Min(0)
    private Long animalsArrived;

    @NotNull
    @Min(0)
    private Long animalsGone;

    public void increaseQuantity() {
        quantityAnimals++;
    }

    public void increaseArrived() {
        animalsArrived++;
    }

    public void increaseGone() {
        animalsGone++;
    }

    public AnimalsAnalytics(AnimalType type) {
        this.animalType = type.getType();
        this.animalTypeId = type.getId();
        this.quantityAnimals = 0L;
        this.animalsArrived = 0L;
        this.animalsGone = 0L;
    }
}
