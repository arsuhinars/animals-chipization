package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.DependsOnException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.model.Animal;
import com.arsuhinars.animals_chipization.model.AnimalType;
import com.arsuhinars.animals_chipization.repository.AnimalTypeRepository;
import com.arsuhinars.animals_chipization.schema.animal.type.AnimalTypeSchema;
import com.arsuhinars.animals_chipization.util.ErrorDetailsFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalTypeServiceImpl implements AnimalTypeService {
    @Autowired
    private AnimalTypeRepository repository;

    @Override
    public AnimalTypeSchema create(AnimalTypeSchema animalType) throws AlreadyExistException {
        if (repository.existsByType(animalType.getType())) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(AnimalType.class, "type", animalType.getType())
            );
        }

        var dbAnimalType = new AnimalType(animalType.getType());

        return AnimalTypeSchema.createFromModel(repository.save(dbAnimalType));
    }

    @Override
    public AnimalTypeSchema getById(Long id) {
        return repository.findById(id).map(AnimalTypeSchema::createFromModel).orElse(null);
    }

    @Override
    public AnimalTypeSchema update(
        Long id, AnimalTypeSchema animalType
    ) throws NotFoundException, AlreadyExistException {
        var dbAnimalType = repository.findById(id).orElse(null);
        if (dbAnimalType == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(AnimalType.class, id)
            );
        }

        if (!dbAnimalType.getType().equals(animalType.getType()) &&
            repository.existsByType(animalType.getType())
        ) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(AnimalType.class, "type", animalType.getType())
            );
        }

        dbAnimalType.setType(animalType.getType());

        return AnimalTypeSchema.createFromModel(repository.save(dbAnimalType));
    }

    @Override
    public void delete(Long id) throws NotFoundException, DependsOnException {
        var dbAnimalType = repository.findById(id).orElse(null);
        if (dbAnimalType == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(AnimalType.class, id)
            );
        }

        if (!dbAnimalType.getAnimals().isEmpty()) {
            throw new DependsOnException(
                ErrorDetailsFormatter.formatDependsOnError(dbAnimalType, Animal.class)
            );
        }

        repository.delete(dbAnimalType);
    }
}
