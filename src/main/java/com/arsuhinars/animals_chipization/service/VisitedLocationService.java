package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.IntegrityBreachException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationSchema;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationUpdateSchema;

import java.time.OffsetDateTime;
import java.util.List;

public interface VisitedLocationService {
    AnimalLocationSchema create(
        Long animalId, Long pointId
    ) throws NotFoundException, IntegrityBreachException;

    List<AnimalLocationSchema> search(
        Long animalId, OffsetDateTime start, OffsetDateTime end, int from, int size
    ) throws NotFoundException;

    AnimalLocationSchema update(
        Long animalId, AnimalLocationUpdateSchema location
    ) throws NotFoundException, IntegrityBreachException;

    void delete(Long animalId, Long locationId) throws NotFoundException;
}
