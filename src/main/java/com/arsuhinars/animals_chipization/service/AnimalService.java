package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.*;
import com.arsuhinars.animals_chipization.schema.animal.AnimalCreateSchema;
import com.arsuhinars.animals_chipization.schema.animal.AnimalSchema;
import com.arsuhinars.animals_chipization.schema.animal.AnimalUpdateSchema;

public interface AnimalService {
    AnimalSchema create(AnimalCreateSchema animal) throws NotFoundException;

    AnimalSchema getById(Long id);

    AnimalSchema update(Long id, AnimalUpdateSchema animal) throws NotFoundException;

    void delete(Long id) throws NotFoundException, BoundException;

    AnimalSchema addType(Long animalId, Long typeId) throws NotFoundException, AlreadyExistException;

    AnimalSchema updateType(Long animalId, Long oldTypeId, Long newTypeId) throws NotFoundException, AlreadyExistException;

    AnimalSchema deleteType(Long animalId, Long typeId) throws NotFoundException, LastItemException;
}
