package com.arsuhinars.animals_chipization.animal.service;

import com.arsuhinars.animals_chipization.core.exception.IntegrityBreachException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.animal.model.Animal;
import com.arsuhinars.animals_chipization.animal.model.AnimalLocation;
import com.arsuhinars.animals_chipization.animal.enums.LifeStatus;
import com.arsuhinars.animals_chipization.location.model.Location;
import com.arsuhinars.animals_chipization.animal.repository.AnimalRepository;
import com.arsuhinars.animals_chipization.animal.repository.AnimalLocationRepository;
import com.arsuhinars.animals_chipization.location.repository.LocationRepository;
import com.arsuhinars.animals_chipization.animal.schema.animal_location.AnimalLocationSchema;
import com.arsuhinars.animals_chipization.animal.schema.animal_location.AnimalLocationUpdateSchema;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import com.arsuhinars.animals_chipization.core.util.OffsetPageable;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AnimalLocationServiceImpl implements AnimalLocationService {
    private final AnimalLocationRepository repository;
    private final AnimalRepository animalRepository;
    private final LocationRepository locationRepository;

    public AnimalLocationServiceImpl(
        AnimalLocationRepository animalLocationRepository,
        AnimalRepository animalRepository,
        LocationRepository locationRepository
    ) {
        this.repository = animalLocationRepository;
        this.animalRepository = animalRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public AnimalLocationSchema create(
        Long animalId, Long pointId
    ) throws NotFoundException, IntegrityBreachException {
        var animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, animalId)
            );
        }

        var point = locationRepository.findById(pointId).orElse(null);
        if (point == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Location.class, pointId)
            );
        }

        if (animal.getLifeStatus() == LifeStatus.DEAD) {
            throw new IntegrityBreachException(
                "Unable to add AnimalVisitedLocation on " + animal + " with lifeStatus=DEAD"
            );
        }

        var animalLastPoint = repository.getAnimalLastPoint(animalId).orElse(null);
        if (animalLastPoint != null &&
            animalLastPoint.getVisitedLocation().equals(point)
        ) {
            throw new IntegrityBreachException(
                "AnimalVisitedLocation with locationPointId=" +
                    pointId + " equals to last item of visitedLocations of " + animal
            );
        }

        if (animalLastPoint == null &&
            point.equals(animal.getChippingLocation())
        ) {
            throw new IntegrityBreachException("First visited point can't be equal to chippingLocation of " + animal);
        }

        var animalLocation = new AnimalLocation(
            animal, point, OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );

        return new AnimalLocationSchema(repository.save(animalLocation));
    }

    @Override
    public List<AnimalLocationSchema> search(
        Long animalId,
        @Nullable OffsetDateTime start,
        @Nullable OffsetDateTime end,
        int from, int size
    ) throws NotFoundException {
        if (!animalRepository.existsById(animalId)) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, animalId)
            );
        }

        return
            repository.search(
                animalId, start, end, new OffsetPageable(size, from, Sort.by("visitedAt", "id").ascending())
            )
            .stream()
            .map(AnimalLocationSchema::new)
            .toList();
    }

    @Override
    public AnimalLocationSchema update(
        Long animalId, AnimalLocationUpdateSchema schema
    ) throws NotFoundException, IntegrityBreachException {
        var animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, animalId)
            );
        }

        var animalLocation = repository.findById(schema.getVisitedLocationPointId()).orElse(null);
        if (animalLocation == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(
                    AnimalLocation.class, schema.getVisitedLocationPointId()
                )
            );
        }

        var animalVisitedLocations = repository.getSortedAnimalPoints(animalId);

        var locationIndex = animalVisitedLocations.indexOf(animalLocation);
        if (locationIndex < 0) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatDoesNotContainError(animal, animalLocation, "visitedLocations")
            );
        }

        var point = locationRepository.findById(schema.getLocationPointId()).orElse(null);
        if (point == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(
                    Location.class, schema.getLocationPointId()
                )
            );
        }

        if (locationIndex == 0 && point.equals(animal.getChippingLocation())) {
            throw new IntegrityBreachException("First visited point can't be equal to chippingLocation of " + animal);
        }

        if (point.equals(animalLocation.getVisitedLocation())) {
            throw new IntegrityBreachException(
                ErrorDetailsFormatter.formatAlreadyContainsError(animal, animalLocation, "visitedLocations")
            );
        }

        if (locationIndex < animalVisitedLocations.size() - 1 &&
            point.equals(animalVisitedLocations.get(locationIndex + 1).getVisitedLocation())
        ) {
            throw new IntegrityBreachException(
                "The next AnimalVisitedLocation is the same in visitedLocations of " + animal
            );
        }

        if (locationIndex > 0 &&
            point.equals(animalVisitedLocations.get(locationIndex - 1).getVisitedLocation())
        ) {
            throw new IntegrityBreachException(
                "The previous AnimalVisitedLocation is the same in visitedLocations of " + animal
            );
        }

        animalLocation.setVisitedLocation(point);

        return new AnimalLocationSchema(repository.save(animalLocation));
    }

    @Override
    public void delete(Long animalId, Long locationId) throws NotFoundException {
        var animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, animalId)
            );
        }

        var visitedLocation = repository.findById(locationId).orElse(null);
        if (visitedLocation == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(AnimalLocation.class, locationId)
            );
        }

        var animalVisitedLocations = repository.getSortedAnimalPoints(animalId);

        var locationIndex = animalVisitedLocations.indexOf(visitedLocation);
        if (locationIndex < 0) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatDoesNotContainError(animal, visitedLocation, "visitedLocations")
            );
        }

        if (locationIndex == 0 &&
            animalVisitedLocations.size() > 1 &&
            animal.getChippingLocation().equals(animalVisitedLocations.get(1).getVisitedLocation())
        ) {
            animal.getVisitedLocations().remove(animalVisitedLocations.get(1));
        }

        animal.getVisitedLocations().remove(visitedLocation);

        animalRepository.save(animal);
    }
}
