package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.*;
import com.arsuhinars.animals_chipization.model.AnimalGender;
import com.arsuhinars.animals_chipization.model.LifeStatus;
import com.arsuhinars.animals_chipization.schema.animal.AnimalCreateSchema;
import com.arsuhinars.animals_chipization.schema.animal.AnimalSchema;
import com.arsuhinars.animals_chipization.schema.animal.AnimalUpdateSchema;

import java.time.OffsetDateTime;
import java.util.List;

public interface AnimalService {
    AnimalSchema create(AnimalCreateSchema animal) throws NotFoundException, AlreadyExistException;

    AnimalSchema getById(Long id);

    List<AnimalSchema> search(
        OffsetDateTime start,
        OffsetDateTime end,
        Long chipperId,
        Long chippingLocationId,
        LifeStatus lifeStatus,
        AnimalGender gender,
        int from, int size
    );

    AnimalSchema update(Long id, AnimalUpdateSchema animal) throws NotFoundException, IntegrityBreachException;

    void delete(Long id) throws NotFoundException, BoundException;

    AnimalSchema addType(Long animalId, Long typeId) throws NotFoundException, AlreadyExistException;

    AnimalSchema updateType(Long animalId, Long oldTypeId, Long newTypeId) throws NotFoundException, AlreadyExistException;

    AnimalSchema deleteType(Long animalId, Long typeId) throws NotFoundException, LastItemException;
}
