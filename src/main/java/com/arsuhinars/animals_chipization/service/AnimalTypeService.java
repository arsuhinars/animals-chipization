package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.BoundException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.animal.AnimalTypeSchema;

public interface AnimalTypeService {
    AnimalTypeSchema create(AnimalTypeSchema type) throws AlreadyExistException;

    AnimalTypeSchema getById(Long id);

    AnimalTypeSchema update(AnimalTypeSchema type) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, BoundException;
}
