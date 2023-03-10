package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.IntegrityBreachException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.model.AnimalVisitedLocation;
import com.arsuhinars.animals_chipization.model.LifeStatus;
import com.arsuhinars.animals_chipization.repository.AnimalRepository;
import com.arsuhinars.animals_chipization.repository.AnimalVisitedLocationRepository;
import com.arsuhinars.animals_chipization.repository.LocationRepository;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationCreateSchema;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationSchema;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationUpdateSchema;
import com.arsuhinars.animals_chipization.util.OffsetPageable;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnimalLocationServiceImpl implements AnimalLocationService {
    @Autowired
    private AnimalVisitedLocationRepository repository;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public AnimalLocationSchema create(
        Long animalId, AnimalLocationCreateSchema location
    ) throws NotFoundException, IntegrityBreachException {
        var animal = animalRepository.findById(animalId);
        if (animal.isEmpty()) {
            throw new NotFoundException("Animal with id " + animalId + " was not found");
        }

        var point = locationRepository.findById(location.getPointId());
        if (point.isEmpty()) {
            throw new NotFoundException("Location with id " + location.getPointId() + " was not found");
        }

        if (animal.get().getLifeStatus() == LifeStatus.DEAD) {
            throw new IntegrityBreachException("Unable to add visited point when animal life status is DEAD");
        }

        var animalLastPoint = repository.getAnimalLastPoint(animalId);
        if (animalLastPoint.isPresent() &&
            animalLastPoint.get().getId().equals(location.getPointId())
        ) {
            throw new IntegrityBreachException("Unable to add point where animal is already situated");
        }

        if (animalLastPoint.isEmpty() &&
            location.getPointId().equals(animal.get().getChippingLocation().getId())
        ) {
            throw new IntegrityBreachException("Unable to add point where animal was chipped");
        }

        return AnimalLocationSchema.createFromModel(
            repository.save(new AnimalVisitedLocation(
                animal.get(),
                point.get(),
                LocalDateTime.now()
            ))
        );
    }

    @Override
    public List<AnimalLocationSchema> search(Long animalId, LocalDateTime start, LocalDateTime end, int from, int size) {
        return repository.search(animalId, start, end, new OffsetPageable(size, from))
            .stream()
            .map(AnimalLocationSchema::createFromModel)
            .toList();
    }

    @Override
    public AnimalLocationSchema update(
        Long animalId, AnimalLocationUpdateSchema location
    ) throws NotFoundException, IntegrityBreachException {
        var animal = animalRepository.findById(animalId);
        if (animal.isEmpty()) {
            throw new NotFoundException("Animal with id " + animalId + " was not found");
        }

        var visitedPoint = repository.findById(
            location.getVisitedLocationPointId()
        );
        if (visitedPoint.isEmpty()) {
            throw new NotFoundException(
                "Animal visited location with id " + location.getVisitedLocationPointId() + " was not found"
            );
        }

        var animalVisitedLocations = repository.getSortedAnimalPoints(animalId);
        var locationIndex = Collections.binarySearch(
            animalVisitedLocations,
            visitedPoint.get(),
            Comparator.comparing(AnimalVisitedLocation::getVisitedAt)
        );
        if (locationIndex < 0) {
            throw new NotFoundException(
                "Animal doesn't have visited location with id " + location.getVisitedLocationPointId()
            );
        }

        var point = locationRepository.findById(location.getLocationPointId());
        if (point.isEmpty()) {
            throw new NotFoundException(
                "Location with id " + location.getLocationPointId() + " was not found"
            );
        }

        if (locationIndex == 0 &&
            location.getLocationPointId().equals(
                animal.get().getChippingLocation().getId()
            )
        ) {
            throw new IntegrityBreachException("First point can't be equal to animal chipping location");
        }

        if (location.getLocationPointId().equals(
            animalVisitedLocations.get(locationIndex).getId()
        )) {
            throw new IntegrityBreachException("Unable to change point to the same one");
        }

        if (locationIndex < animalVisitedLocations.size() - 1 &&
            location.getLocationPointId().equals(
                animalVisitedLocations.get(locationIndex + 1).getId()
            )
        ) {
            throw new IntegrityBreachException("The next point is the same");
        }

        if (locationIndex > 0 &&
            location.getLocationPointId().equals(
                animalVisitedLocations.get(locationIndex - 1).getId()
            )
        ) {
            throw new IntegrityBreachException("The previous point is the same");
        }

        var dbPoint = visitedPoint.get();
        dbPoint.setVisitedLocation(point.get());
        dbPoint.setVisitedAt(LocalDateTime.now());

        return AnimalLocationSchema.createFromModel(
            repository.save(dbPoint)
        );
    }

    @Override
    public void delete(Long animalId, Long locationId) throws NotFoundException {
        var animal = animalRepository.findById(animalId);
        if (animal.isEmpty()) {
            throw new NotFoundException("Animal with id " + animalId + " was not found");
        }

        var visitedPoint = repository.findById(locationId);
        if (visitedPoint.isEmpty()) {
            throw new NotFoundException(
                "Animal visited location with id " + locationId + " was not found"
            );
        }

        var animalVisitedLocations = repository.getSortedAnimalPoints(animalId);
        var locationIndex = Collections.binarySearch(
            animalVisitedLocations,
            visitedPoint.get(),
            Comparator.comparing(AnimalVisitedLocation::getVisitedAt)
        );
        if (locationIndex < 0) {
            throw new NotFoundException(
                "Animal doesn't have visited location with id " + locationId
            );
        }

        repository.deleteById(locationId);
        if (locationIndex == 0 &&
            animal.get().getChippingLocation().getId().equals(
                animalVisitedLocations.get(locationIndex + 1).getId()
            )
        ) {
            repository.delete(animalVisitedLocations.get(locationIndex + 1));
        }
    }
}
