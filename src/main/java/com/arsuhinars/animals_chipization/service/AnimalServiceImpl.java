package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.*;
import com.arsuhinars.animals_chipization.model.*;
import com.arsuhinars.animals_chipization.repository.AccountRepository;
import com.arsuhinars.animals_chipization.repository.AnimalRepository;
import com.arsuhinars.animals_chipization.repository.AnimalTypeRepository;
import com.arsuhinars.animals_chipization.repository.LocationRepository;
import com.arsuhinars.animals_chipization.schema.animal.AnimalCreateSchema;
import com.arsuhinars.animals_chipization.schema.animal.AnimalSchema;
import com.arsuhinars.animals_chipization.schema.animal.AnimalUpdateSchema;
import com.arsuhinars.animals_chipization.util.OffsetPageable;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnimalServiceImpl implements AnimalService {
    @Autowired
    private AnimalRepository repository;
    @Autowired
    private AnimalTypeRepository animalTypeRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public AnimalSchema create(AnimalCreateSchema animal) throws NotFoundException, AlreadyExistException {
        var chipper = accountRepository.findById(animal.getChipperId());
        if (chipper.isEmpty()) {
            throw new NotFoundException(
                "Chipper account with id " + animal.getChipperId() + " was not found"
            );
        }

        var chippingLocation = locationRepository.findById(
            animal.getChippingLocationId()
        );
        if (chippingLocation.isEmpty()) {
            throw new NotFoundException(
                "Chipping location with id " + animal.getChippingLocationId() + " was not found"
            );
        }

        var types = new HashSet<AnimalType>();
        for (var typeId : animal.getAnimalTypes()) {
            var result = animalTypeRepository.findById(typeId);
            if (result.isEmpty()) {
                throw new NotFoundException("Animal type with id " + typeId + " was not found");
            }
            if (!types.add(result.get())) {
                throw new AlreadyExistException("Animal type with id " + typeId + " is already given");
            }
        }

        return AnimalSchema.createFromModel(
            repository.save(new Animal(
                types,
                animal.getWeight(),
                animal.getLength(),
                animal.getHeight(),
                animal.getGender(),
                LifeStatus.ALIVE,
                LocalDateTime.now(),
                chipper.get(),
                chippingLocation.get(),
                Set.of()
            ))
        );
    }

    @Override
    public AnimalSchema getById(Long id) {
        return repository.findById(id).map(AnimalSchema::createFromModel).orElse(null);
    }

    @Override
    public List<AnimalSchema> search(
        LocalDateTime start,
        LocalDateTime end,
        Long chipperId,
        Long chippingLocationId,
        LifeStatus lifeStatus,
        AnimalGender gender,
        int from, int size
    ) {
        return repository.search(
            start, end, chipperId, chippingLocationId, lifeStatus, gender, new OffsetPageable(size, from)
            ).stream()
            .map(AnimalSchema::createFromModel)
            .toList();
    }

    @Override
    public AnimalSchema update(Long id, AnimalUpdateSchema animal) throws NotFoundException {
        var result = repository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Animal with id " + id + " was not found");
        }

        var dbAnimal = result.get();

        if (dbAnimal.getLifeStatus() == LifeStatus.DEAD &&
            animal.getLifeStatus() == LifeStatus.ALIVE
        ) {
            throw new IntegrityBreachException("Can not set life status to ALIVE when animal is DEAD");
        }

        var chipper = accountRepository.findById(animal.getChipperId());
        if (chipper.isEmpty()) {
            throw new NotFoundException("Chipper account with id " + id + " was not found");
        }

        var chippingLocation = locationRepository.findById(
            animal.getChippingLocationId()
        );
        if (chippingLocation.isEmpty()) {
            throw new NotFoundException("Chipping location with id " + id + " was not found");
        }

        dbAnimal.setWeight(animal.getWeight());
        dbAnimal.setLength(animal.getLength());
        dbAnimal.setHeight(animal.getHeight());
        dbAnimal.setLifeStatus(animal.getLifeStatus());
        dbAnimal.setChipper(chipper.get());
        dbAnimal.setChippingLocation(chippingLocation.get());

        return AnimalSchema.createFromModel(repository.save(dbAnimal));
    }

    @Override
    public void delete(Long id) throws NotFoundException, BoundException {
        var result = repository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Animal with id " + id + " was not found");
        }

        if (!result.get().getVisitedLocations().isEmpty()) {
            throw new BoundException("Animal has visited locations");
        }

        repository.deleteById(id);
    }

    @Override
    public AnimalSchema addType(Long animalId, Long typeId) throws NotFoundException, AlreadyExistException {
        var result = repository.findById(animalId);
        if (result.isEmpty()) {
            throw new NotFoundException("Animal with id " + animalId + " was not found");
        }

        var type = animalTypeRepository.findById(typeId);
        if (type.isEmpty()) {
            throw new NotFoundException("Animal type with id " + typeId + " was not found");
        }

        if (result.get().getTypes().contains(type.get())) {
            throw new AlreadyExistException("Animal already has given animal type");
        }

        var dbAnimal = result.get();
        dbAnimal.getTypes().add(type.get());

        return AnimalSchema.createFromModel(repository.save(dbAnimal));
    }

    @Override
    public AnimalSchema updateType(
        Long animalId, Long oldTypeId, Long newTypeId
    ) throws NotFoundException, AlreadyExistException {
        var result = repository.findById(animalId);
        if (result.isEmpty()) {
            throw new NotFoundException("Animal with id " + animalId + " was not found");
        }

        var oldType = animalTypeRepository.findById(oldTypeId);
        if (oldType.isEmpty()) {
            throw new NotFoundException("Animal type with id " + oldTypeId + " was not found");
        }

        var newType = animalTypeRepository.findById(newTypeId);
        if (newType.isEmpty()) {
            throw new NotFoundException("Animal type with id " + newTypeId + " was not found");
        }

        var dbAnimal = result.get();

        if (!dbAnimal.getTypes().contains(oldType.get())) {
            throw new NotFoundException("Animal do not have type with id " + oldTypeId);
        }

        if (dbAnimal.getTypes().contains(newType.get())) {
            throw new AlreadyExistException("Animal already has type with id " + newTypeId);
        }

        dbAnimal.getTypes().remove(oldType.get());
        dbAnimal.getTypes().add(newType.get());

        return AnimalSchema.createFromModel(repository.save(dbAnimal));
    }

    @Override
    public AnimalSchema deleteType(Long animalId, Long typeId) throws NotFoundException, LastItemException {
        var result = repository.findById(animalId);
        if (result.isEmpty()) {
            throw new NotFoundException("Animal with id " + animalId + " was not found");
        }

        var type = animalTypeRepository.findById(typeId);
        if (type.isEmpty()) {
            throw new NotFoundException("Animal type with id " + typeId + " was not found");
        }

        var dbAnimal = result.get();
        if (!dbAnimal.getTypes().contains(type.get())) {
            throw new NotFoundException("Animal do not have type with id " + typeId);
        }

        if (dbAnimal.getTypes().size() == 1) {
            throw new LastItemException("Type with id " + typeId + " is last type for this animal");
        }

        dbAnimal.getTypes().remove(type.get());

        return AnimalSchema.createFromModel(repository.save(dbAnimal));
    }
}
