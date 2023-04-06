package com.arsuhinars.animals_chipization.animal.service;

import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.DependsOnException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.animal.schema.animal_type.AnimalTypeCreateSchema;
import com.arsuhinars.animals_chipization.animal.schema.animal_type.AnimalTypeSchema;
import com.arsuhinars.animals_chipization.animal.schema.animal_type.AnimalTypeUpdateSchema;

import java.util.Optional;

public interface AnimalTypeService {
    AnimalTypeSchema create(AnimalTypeCreateSchema schema) throws AlreadyExistException;

    Optional<AnimalTypeSchema> getById(Long id);

    AnimalTypeSchema update(Long id, AnimalTypeUpdateSchema schema) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, DependsOnException;
}
