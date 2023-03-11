package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.BoundException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.model.AnimalType;
import com.arsuhinars.animals_chipization.repository.AnimalTypeRepository;
import com.arsuhinars.animals_chipization.schema.animal.type.AnimalTypeSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalTypeServiceImpl implements AnimalTypeService {
    @Autowired
    private AnimalTypeRepository repository;

    @Override
    public AnimalTypeSchema create(AnimalTypeSchema animalType) throws AlreadyExistException {
        if (repository.existsByType(animalType.getType())) {
            throw new AlreadyExistException("Animal type \"" + animalType.getType() + "\" already exists");
        }

        return AnimalTypeSchema.createFromModel(
            repository.save(new AnimalType(
                animalType.getType()
            ))
        );
    }

    @Override
    public AnimalTypeSchema getById(Long id) {
        return repository.findById(id).map(AnimalTypeSchema::createFromModel).orElse(null);
    }

    @Override
    public AnimalTypeSchema update(
        Long id, AnimalTypeSchema animalType
    ) throws NotFoundException, AlreadyExistException {
        var result = repository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Animal type with id " + id + " was not found");
        }

        if (repository.existsByType(animalType.getType())) {
            throw new AlreadyExistException("Animal type \"" + animalType.getType() + "\" already exists");
        }

        var dbAnimalType = result.get();
        dbAnimalType.setType(animalType.getType());

        return AnimalTypeSchema.createFromModel(repository.save(dbAnimalType));
    }

    @Override
    public void delete(Long id) throws NotFoundException, BoundException {
        var result = repository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Animal type with id " + id + " was not found");
        }

        if (!result.get().getAnimals().isEmpty()) {
            throw new BoundException("Animal type is used by some animal");
        }

        repository.deleteById(id);
    }
}
