package com.arsuhinars.animals_chipization.area.schema;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AreaAnalyticsSchema {
    @NotNull
    @Min(0)
    private Long totalQuantityAnimals;

    @NotNull
    @Min(0)
    private Long totalAnimalsArrived;

    @NotNull
    @Min(0)
    private Long totalAnimalsGone;

    @NotNull
    private List<AnimalsAnalytics> animalsAnalytics;

    public void increaseQuantity() {
        totalQuantityAnimals++;
    }

    public void increaseArrived() {
        totalAnimalsArrived++;
    }

    public void increaseGone() {
        totalAnimalsGone++;
    }

    public AreaAnalyticsSchema() {
        this.totalQuantityAnimals = 0L;
        this.totalAnimalsArrived = 0L;
        this.totalAnimalsGone = 0L;
        this.animalsAnalytics = null;
    }
}
