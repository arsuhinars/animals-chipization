package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.IntegrityBreachException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationCreateSchema;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationSchema;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationUpdateSchema;

import java.time.LocalDateTime;
import java.util.List;

public interface AnimalLocationService {
    AnimalLocationSchema create(
        Long animalId, AnimalLocationCreateSchema location
    ) throws NotFoundException, IntegrityBreachException;

    List<AnimalLocationSchema> search(Long animalId, LocalDateTime start, LocalDateTime end, int from, int size);

    AnimalLocationSchema update(
        Long animalId, AnimalLocationUpdateSchema location
    ) throws NotFoundException, IntegrityBreachException;

    void delete(Long animalId, Long locationId) throws NotFoundException;
}
