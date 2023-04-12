package com.arsuhinars.animals_chipization.animal.service;

import com.arsuhinars.animals_chipization.animal.model.Animal;
import com.arsuhinars.animals_chipization.core.exception.*;
import com.arsuhinars.animals_chipization.animal.enums.Gender;
import com.arsuhinars.animals_chipization.animal.enums.LifeStatus;
import com.arsuhinars.animals_chipization.animal.schema.animal.AnimalCreateSchema;
import com.arsuhinars.animals_chipization.animal.schema.animal.AnimalUpdateSchema;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface AnimalService {
    Animal create(AnimalCreateSchema schema) throws NotFoundException, AlreadyExistException;

    Optional<Animal> getById(Long id);

    List<Animal> search(
        @Nullable OffsetDateTime start,
        @Nullable OffsetDateTime end,
        @Nullable Long chipperId,
        @Nullable Long chippingLocationId,
        @Nullable LifeStatus lifeStatus,
        @Nullable Gender gender,
        int from, int size
    );

    Animal update(Long id, AnimalUpdateSchema schema) throws NotFoundException, IntegrityBreachException;

    void delete(Long id) throws NotFoundException, DependsOnException;

    Animal addType(Long animalId, Long typeId) throws NotFoundException, AlreadyExistException;

    Animal updateType(Long animalId, Long oldTypeId, Long newTypeId) throws NotFoundException, AlreadyExistException;

    Animal deleteType(Long animalId, Long typeId) throws NotFoundException, LastItemException;
}
