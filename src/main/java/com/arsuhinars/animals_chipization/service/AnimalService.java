package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.*;
import com.arsuhinars.animals_chipization.enums.Gender;
import com.arsuhinars.animals_chipization.enums.LifeStatus;
import com.arsuhinars.animals_chipization.schema.animal.AnimalCreateSchema;
import com.arsuhinars.animals_chipization.schema.animal.AnimalSchema;
import com.arsuhinars.animals_chipization.schema.animal.AnimalUpdateSchema;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface AnimalService {
    AnimalSchema create(AnimalCreateSchema schema) throws NotFoundException, AlreadyExistException;

    Optional<AnimalSchema> getById(Long id);

    List<AnimalSchema> search(
        @Nullable OffsetDateTime start,
        @Nullable OffsetDateTime end,
        @Nullable Long chipperId,
        @Nullable Long chippingLocationId,
        @Nullable LifeStatus lifeStatus,
        @Nullable Gender gender,
        int from, int size
    );

    AnimalSchema update(Long id, AnimalUpdateSchema schema) throws NotFoundException, IntegrityBreachException;

    void delete(Long id) throws NotFoundException, DependsOnException;

    AnimalSchema addType(Long animalId, Long typeId) throws NotFoundException, AlreadyExistException;

    AnimalSchema updateType(Long animalId, Long oldTypeId, Long newTypeId) throws NotFoundException, AlreadyExistException;

    AnimalSchema deleteType(Long animalId, Long typeId) throws NotFoundException, LastItemException;
}
