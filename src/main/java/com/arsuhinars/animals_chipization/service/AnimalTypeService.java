package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.DependsOnException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.animal.type.AnimalTypeSchema;

public interface AnimalTypeService {
    AnimalTypeSchema create(AnimalTypeSchema animalType) throws AlreadyExistException;

    AnimalTypeSchema getById(Long id);

    AnimalTypeSchema update(Long id, AnimalTypeSchema animalType) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, DependsOnException;
}
