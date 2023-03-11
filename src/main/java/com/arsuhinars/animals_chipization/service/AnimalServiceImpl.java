package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.*;
import com.arsuhinars.animals_chipization.model.*;
import com.arsuhinars.animals_chipization.repository.*;
import com.arsuhinars.animals_chipization.schema.animal.AnimalCreateSchema;
import com.arsuhinars.animals_chipization.schema.animal.AnimalSchema;
import com.arsuhinars.animals_chipization.schema.animal.AnimalUpdateSchema;
import com.arsuhinars.animals_chipization.util.OffsetPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AnimalServiceImpl implements AnimalService {
    @Autowired
    private AnimalRepository repository;
    @Autowired
    private AnimalTypeRepository animalTypeRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private AnimalVisitedLocationRepository animalLocationRepository;

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
                OffsetDateTime.now(),
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
        OffsetDateTime start,
        OffsetDateTime end,
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
    public AnimalSchema update(Long id, AnimalUpdateSchema animal) throws NotFoundException, IntegrityBreachException {
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

        var animalFirstPoint = animalLocationRepository.getAnimalFirstPoint(id);
        if (animalFirstPoint.isPresent() &&
            chippingLocation.get().getId().equals(animalFirstPoint.get().getVisitedLocation().getId())
        ) {
            throw new IntegrityBreachException("Unable to change animal chipping location to first visited point");
        }

        dbAnimal.setWeight(animal.getWeight());
        dbAnimal.setLength(animal.getLength());
        dbAnimal.setHeight(animal.getHeight());
        dbAnimal.setGender(animal.getGender());
        dbAnimal.setChipper(chipper.get());
        dbAnimal.setChippingLocation(chippingLocation.get());

        if (dbAnimal.getLifeStatus() == LifeStatus.ALIVE &&
            animal.getLifeStatus() == LifeStatus.DEAD
        ) {
            dbAnimal.setLifeStatus(LifeStatus.DEAD);
            dbAnimal.setDeathDateTime(OffsetDateTime.now());
        }

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
