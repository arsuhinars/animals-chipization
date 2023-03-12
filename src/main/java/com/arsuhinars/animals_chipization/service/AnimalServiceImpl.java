package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.enums.Gender;
import com.arsuhinars.animals_chipization.enums.LifeStatus;
import com.arsuhinars.animals_chipization.exception.*;
import com.arsuhinars.animals_chipization.model.*;
import com.arsuhinars.animals_chipization.repository.*;
import com.arsuhinars.animals_chipization.schema.animal.AnimalCreateSchema;
import com.arsuhinars.animals_chipization.schema.animal.AnimalSchema;
import com.arsuhinars.animals_chipization.schema.animal.AnimalUpdateSchema;
import com.arsuhinars.animals_chipization.util.ErrorDetailsFormatter;
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
    private VisitedLocationRepository visitedLocationRepository;

    @Override
    public AnimalSchema create(AnimalCreateSchema animal) throws NotFoundException, AlreadyExistException {
        var chipper = accountRepository.findById(animal.getChipperId()).orElse(null);
        if (chipper == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Account.class, animal.getChipperId())
            );
        }

        var chippingLocation = locationRepository.findById(
            animal.getChippingLocationId()
        ).orElse(null);
        if (chippingLocation == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Location.class, animal.getChippingLocationId())
            );
        }

        var animalTypes = new HashSet<AnimalType>();
        for (var animalTypeId : animal.getAnimalTypes()) {
            var animalType = animalTypeRepository.findById(animalTypeId).orElse(null);
            if (animalType == null) {
                throw new NotFoundException(
                    ErrorDetailsFormatter.formatNotFoundError(AnimalType.class, animalTypeId)
                );
            }
            animalTypes.add(animalType);
        }

        var dbAnimal = new Animal(
            animalTypes,
            animal.getWeight(),
            animal.getLength(),
            animal.getHeight(),
            animal.getGender(),
            LifeStatus.ALIVE,
            OffsetDateTime.now(),
            chipper,
            chippingLocation,
            Set.of()
        );

        return AnimalSchema.createFromModel(repository.save(dbAnimal));
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
        Gender gender,
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
        var dbAnimal = repository.findById(id).orElse(null);
        if (dbAnimal == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, id)
            );
        }

        if (dbAnimal.getLifeStatus() == LifeStatus.DEAD &&
            animal.getLifeStatus() == LifeStatus.ALIVE) {
            throw new IntegrityBreachException(
                "Unable to change lifeStatus from DEAD to ALIVE on " + dbAnimal
            );
        }

        var chipper = accountRepository.findById(animal.getChipperId()).orElse(null);
        if (chipper == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Account.class, animal.getChipperId())
            );
        }

        var chippingLocation = locationRepository.findById(
            animal.getChippingLocationId()
        ).orElse(null);
        if (chippingLocation == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Location.class, animal.getChippingLocationId())
            );
        }

        var animalFirstPoint = visitedLocationRepository.getAnimalFirstPoint(id).orElse(null);
        if (animalFirstPoint != null &&
            chippingLocation.equals(animalFirstPoint.getVisitedLocation())
        ) {
            throw new IntegrityBreachException(
                "Unable to change chippingLocation to first visited point on " + dbAnimal
            );
        }

        dbAnimal.setWeight(animal.getWeight());
        dbAnimal.setLength(animal.getLength());
        dbAnimal.setHeight(animal.getHeight());
        dbAnimal.setGender(animal.getGender());
        dbAnimal.setChipper(chipper);
        dbAnimal.setChippingLocation(chippingLocation);

        if (dbAnimal.getLifeStatus() == LifeStatus.ALIVE &&
            animal.getLifeStatus() == LifeStatus.DEAD
        ) {
            dbAnimal.setLifeStatus(LifeStatus.DEAD);
            dbAnimal.setDeathDateTime(OffsetDateTime.now());
        }

        return AnimalSchema.createFromModel(repository.save(dbAnimal));
    }

    @Override
    public void delete(Long id) throws NotFoundException, DependsOnException {
        var dbAnimal = repository.findById(id).orElse(null);
        if (dbAnimal == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, id)
            );
        }

        if (!dbAnimal.getVisitedLocations().isEmpty()) {
            throw new DependsOnException(
                ErrorDetailsFormatter.formatDependsOnError(dbAnimal, AnimalVisitedLocation.class)
            );
        }

        dbAnimal.getTypes().clear();

        repository.delete(dbAnimal);
    }

    @Override
    public AnimalSchema addType(Long animalId, Long typeId) throws NotFoundException, AlreadyExistException {
        var dbAnimal = repository.findById(animalId).orElse(null);
        if (dbAnimal == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, animalId)
            );
        }

        var animalType = animalTypeRepository.findById(typeId).orElse(null);
        if (animalType == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(AnimalType.class, typeId)
            );
        }

        if (dbAnimal.getTypes().contains(animalType)) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyContainsError(dbAnimal, animalType, "types")
            );
        }

        dbAnimal.getTypes().add(animalType);

        return AnimalSchema.createFromModel(repository.save(dbAnimal));
    }

    @Override
    public AnimalSchema updateType(
        Long animalId, Long oldTypeId, Long newTypeId
    ) throws NotFoundException, AlreadyExistException {
        var dbAnimal = repository.findById(animalId).orElse(null);
        if (dbAnimal == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, animalId)
            );
        }

        var oldType = animalTypeRepository.findById(oldTypeId).orElse(null);
        if (oldType == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(AnimalType.class, oldTypeId)
            );
        }

        var newType = animalTypeRepository.findById(newTypeId).orElse(null);
        if (newType == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(AnimalType.class, newTypeId)
            );
        }

        if (!dbAnimal.getTypes().contains(oldType)) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatDoesNotContainError(dbAnimal, oldType, "types")
            );
        }

        if (dbAnimal.getTypes().contains(newType)) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyContainsError(dbAnimal, newType, "types")
            );
        }

        dbAnimal.getTypes().remove(oldType);
        dbAnimal.getTypes().add(newType);

        return AnimalSchema.createFromModel(repository.save(dbAnimal));
    }

    @Override
    public AnimalSchema deleteType(Long animalId, Long typeId) throws NotFoundException, LastItemException {
        var dbAnimal = repository.findById(animalId).orElse(null);
        if (dbAnimal == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, animalId)
            );
        }

        var animalType = animalTypeRepository.findById(typeId).orElse(null);
        if (animalType == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(AnimalType.class, typeId)
            );
        }

        if (!dbAnimal.getTypes().contains(animalType)) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatDoesNotContainError(dbAnimal, animalType, "types")
            );
        }

        if (dbAnimal.getTypes().size() == 1) {
            throw new LastItemException(
                ErrorDetailsFormatter.formatLastItemError(dbAnimal, animalType, "types")
            );
        }

        dbAnimal.getTypes().remove(animalType);

        return AnimalSchema.createFromModel(repository.save(dbAnimal));
    }
}
