package com.arsuhinars.animals_chipization.animal.service;

import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.DependsOnException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.animal.model.Animal;
import com.arsuhinars.animals_chipization.animal.model.AnimalType;
import com.arsuhinars.animals_chipization.animal.repository.AnimalTypeRepository;
import com.arsuhinars.animals_chipization.animal.schema.animal_type.AnimalTypeCreateSchema;
import com.arsuhinars.animals_chipization.animal.schema.animal_type.AnimalTypeSchema;
import com.arsuhinars.animals_chipization.animal.schema.animal_type.AnimalTypeUpdateSchema;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnimalTypeServiceImpl implements AnimalTypeService {
    private final AnimalTypeRepository repository;

    public AnimalTypeServiceImpl(AnimalTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public AnimalTypeSchema create(AnimalTypeCreateSchema schema) throws AlreadyExistException {
        if (repository.existsByType(schema.getType())) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(AnimalType.class, "type", schema.getType())
            );
        }

        var animalType = new AnimalType(schema.getType());

        return new AnimalTypeSchema(repository.save(animalType));
    }

    @Override
    public Optional<AnimalTypeSchema> getById(Long id) {
        return repository.findById(id).map(AnimalTypeSchema::new);
    }

    @Override
    public AnimalTypeSchema update(Long id, AnimalTypeUpdateSchema schema) throws NotFoundException, AlreadyExistException {
        var animalType = repository.findById(id).orElse(null);
        if (animalType == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(AnimalType.class, id)
            );
        }

        if (!animalType.getType().equals(schema.getType()) &&
            repository.existsByType(schema.getType())
        ) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(AnimalType.class, "type", schema.getType())
            );
        }

        animalType.setType(schema.getType());

        return new AnimalTypeSchema(repository.save(animalType));
    }

    @Override
    public void delete(Long id) throws NotFoundException, DependsOnException {
        var animalType = repository.findById(id).orElse(null);
        if (animalType == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(AnimalType.class, id)
            );
        }

        if (!animalType.getAnimals().isEmpty()) {
            throw new DependsOnException(
                ErrorDetailsFormatter.formatDependsOnError(animalType, Animal.class)
            );
        }

        repository.delete(animalType);
    }
}
