package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.DependsOnException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.AnimalTypeSchema;

import java.util.Optional;

public interface AnimalTypeService {
    AnimalTypeSchema create(AnimalTypeSchema schema) throws AlreadyExistException;

    Optional<AnimalTypeSchema> getById(Long id);

    AnimalTypeSchema update(Long id, AnimalTypeSchema schema) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, DependsOnException;
}
