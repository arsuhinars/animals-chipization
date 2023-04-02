package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.IntegrityBreachException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.animal_location.AnimalLocationSchema;
import com.arsuhinars.animals_chipization.schema.animal_location.AnimalLocationUpdateSchema;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;
import java.util.List;

public interface AnimalLocationService {
    AnimalLocationSchema create(Long animalId, Long pointId) throws NotFoundException, IntegrityBreachException;

    List<AnimalLocationSchema> search(
        Long animalId,
        @Nullable OffsetDateTime start,
        @Nullable OffsetDateTime end,
        int from, int size
    ) throws NotFoundException;

    AnimalLocationSchema update(
        Long animalId, AnimalLocationUpdateSchema schema
    ) throws NotFoundException, IntegrityBreachException;

    void delete(Long animalId, Long locationId) throws NotFoundException;
}
