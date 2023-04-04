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
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepository repository;
    private final AnimalTypeRepository animalTypeRepository;
    private final AnimalLocationRepository animalLocationRepository;
    private final AccountRepository accountRepository;
    private final LocationRepository locationRepository;

    public AnimalServiceImpl(
        AnimalRepository animalRepository,
        AnimalTypeRepository animalTypeRepository,
        AnimalLocationRepository animalLocationRepository,
        AccountRepository accountRepository,
        LocationRepository locationRepository
    ) {
        this.repository = animalRepository;
        this.animalTypeRepository = animalTypeRepository;
        this.animalLocationRepository = animalLocationRepository;
        this.accountRepository = accountRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public AnimalSchema create(AnimalCreateSchema schema) throws NotFoundException, AlreadyExistException {
        var chipper = accountRepository.findById(schema.getChipperId()).orElse(null);
        if (chipper == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Account.class, schema.getChipperId())
            );
        }

        var chippingLocation = locationRepository.findById(schema.getChippingLocationId()).orElse(null);
        if (chippingLocation == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Location.class, schema.getChippingLocationId())
            );
        }

        var animalTypes = new HashSet<AnimalType>();
        for (var animalTypeId : schema.getAnimalTypes()) {
            var animalType = animalTypeRepository.findById(animalTypeId).orElse(null);
            if (animalType == null) {
                throw new NotFoundException(
                    ErrorDetailsFormatter.formatNotFoundError(AnimalType.class, animalTypeId)
                );
            }
            animalTypes.add(animalType);
        }

        var animal = new Animal(
            animalTypes,
            schema.getWeight(),
            schema.getLength(),
            schema.getHeight(),
            schema.getGender(),
            LifeStatus.ALIVE,
            OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS),
            chipper,
            chippingLocation,
            Set.of()
        );

        return new AnimalSchema(repository.save(animal));
    }

    @Override
    public Optional<AnimalSchema> getById(Long id) {
        return repository.findById(id).map(AnimalSchema::new);
    }

    @Override
    public List<AnimalSchema> search(
        @Nullable OffsetDateTime start,
        @Nullable OffsetDateTime end,
        @Nullable Long chipperId,
        @Nullable Long chippingLocationId,
        @Nullable LifeStatus lifeStatus,
        @Nullable Gender gender,
        int from, int size
    ) {
        return
            repository.search(
                start, end, chipperId, chippingLocationId, lifeStatus, gender, new OffsetPageable(size, from)
            ).stream()
            .map(AnimalSchema::new)
            .toList();
    }

    @Override
    public AnimalSchema update(Long id, AnimalUpdateSchema schema) throws NotFoundException, IntegrityBreachException {
        var animal = repository.findById(id).orElse(null);
        if (animal == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, id)
            );
        }

        if (animal.getLifeStatus() == LifeStatus.DEAD &&
            schema.getLifeStatus() == LifeStatus.ALIVE
        ) {
            throw new IntegrityBreachException(
                "Unable to change lifeStatus from DEAD to ALIVE on " + animal
            );
        }

        var chipper = accountRepository.findById(schema.getChipperId()).orElse(null);
        if (chipper == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Account.class, schema.getChipperId())
            );
        }

        var chippingLocation = locationRepository.findById(schema.getChippingLocationId()).orElse(null);
        if (chippingLocation == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Location.class, schema.getChippingLocationId())
            );
        }

        var animalFirstPoint = animalLocationRepository.getAnimalFirstPoint(id).orElse(null);
        if (animalFirstPoint != null &&
            chippingLocation.equals(animalFirstPoint.getVisitedLocation())
        ) {
            throw new IntegrityBreachException(
                "Unable to change chippingLocation to first visited point on " + animal
            );
        }

        animal.setWeight(schema.getWeight());
        animal.setLength(schema.getLength());
        animal.setHeight(schema.getHeight());
        animal.setGender(schema.getGender());
        animal.setChipper(chipper);
        animal.setChippingLocation(chippingLocation);

        if (animal.getLifeStatus() == LifeStatus.ALIVE &&
            schema.getLifeStatus() == LifeStatus.DEAD
        ) {
            animal.setLifeStatus(LifeStatus.DEAD);
            animal.setDeathDateTime(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        }

        return new AnimalSchema(repository.save(animal));
    }

    @Override
    public void delete(Long id) throws NotFoundException, DependsOnException {
        var animal = repository.findById(id).orElse(null);
        if (animal == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, id)
            );
        }

        if (!animal.getVisitedLocations().isEmpty()) {
            throw new DependsOnException(
                ErrorDetailsFormatter.formatDependsOnError(animal, AnimalLocation.class)
            );
        }

        animal.getTypes().clear();

        repository.delete(animal);
    }

    @Override
    public AnimalSchema addType(Long animalId, Long typeId) throws NotFoundException, AlreadyExistException {
        var animal = repository.findById(animalId).orElse(null);
        if (animal == null) {
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

        if (animal.getTypes().contains(animalType)) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyContainsError(animal, animalType, "types")
            );
        }

        animal.getTypes().add(animalType);

        return new AnimalSchema(repository.save(animal));
    }

    @Override
    public AnimalSchema updateType(
        Long animalId, Long oldTypeId, Long newTypeId
    ) throws NotFoundException, AlreadyExistException {
        var animal = repository.findById(animalId).orElse(null);
        if (animal == null) {
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

        if (!animal.getTypes().contains(oldType)) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatDoesNotContainError(animal, oldType, "types")
            );
        }

        if (animal.getTypes().contains(newType)) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyContainsError(animal, newType, "types")
            );
        }

        animal.getTypes().remove(oldType);
        animal.getTypes().add(newType);

        return new AnimalSchema(repository.save(animal));
    }

    @Override
    public AnimalSchema deleteType(Long animalId, Long typeId) throws NotFoundException, LastItemException {
        var animal = repository.findById(animalId).orElse(null);
        if (animal == null) {
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

        if (!animal.getTypes().contains(animalType)) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatDoesNotContainError(animal, animalType, "types")
            );
        }

        if (animal.getTypes().size() == 1) {
            throw new LastItemException(
                ErrorDetailsFormatter.formatLastItemError(animal, animalType, "types")
            );
        }

        animal.getTypes().remove(animalType);

        return new AnimalSchema(repository.save(animal));
    }
}
