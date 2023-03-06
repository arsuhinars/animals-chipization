package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AppException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationCreateSchema;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationSchema;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationUpdateSchema;

public interface AnimalLocationService {
    AnimalLocationSchema create(Long animalId, AnimalLocationCreateSchema location) throws AppException;

    AnimalLocationSchema update(Long animalId, AnimalLocationUpdateSchema location) throws AppException;

    void delete(Long animalId, Long locationId) throws NotFoundException;
}
