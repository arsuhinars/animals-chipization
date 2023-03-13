package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.IntegrityBreachException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.model.Animal;
import com.arsuhinars.animals_chipization.model.AnimalVisitedLocation;
import com.arsuhinars.animals_chipization.enums.LifeStatus;
import com.arsuhinars.animals_chipization.model.Location;
import com.arsuhinars.animals_chipization.repository.AnimalRepository;
import com.arsuhinars.animals_chipization.repository.VisitedLocationRepository;
import com.arsuhinars.animals_chipization.repository.LocationRepository;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationSchema;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationUpdateSchema;
import com.arsuhinars.animals_chipization.util.ErrorDetailsFormatter;
import com.arsuhinars.animals_chipization.util.OffsetPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class VisitedLocationServiceImpl implements VisitedLocationService {
    private final String FIRST_ITEM_EQUALS_CHIPPING_LOCATION =
        "Unable to set first AnimalVisitedLocation in visitedLocations that equal to chippingLocation of ";

    @Autowired
    private VisitedLocationRepository repository;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private LocationRepository locationRepository;

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
            throw new IntegrityBreachException(
                FIRST_ITEM_EQUALS_CHIPPING_LOCATION + animal
            );
        }

        var dbLocation = new AnimalVisitedLocation(
            animal, point, OffsetDateTime.now()
        );

        return AnimalLocationSchema.createFromModel(repository.save(dbLocation));
    }

    @Override
    public List<AnimalLocationSchema> search(
        Long animalId, OffsetDateTime start, OffsetDateTime end, int from, int size
    ) throws NotFoundException {
        if (!animalRepository.existsById(animalId)) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, animalId)
            );
        }

        return repository.search(
            animalId, start, end, new OffsetPageable(size, from, Sort.by("visitedAt", "id").ascending())
            )
            .stream()
            .map(AnimalLocationSchema::createFromModel)
            .toList();
    }

    @Override
    public AnimalLocationSchema update(
        Long animalId, AnimalLocationUpdateSchema location
    ) throws NotFoundException, IntegrityBreachException {
        var animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, animalId)
            );
        }

        var visitedLocation = repository.findById(
            location.getVisitedLocationPointId()
        ).orElse(null);
        if (visitedLocation == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(
                    AnimalVisitedLocation.class, location.getVisitedLocationPointId()
                )
            );
        }

        var animalVisitedLocations = repository.getSortedAnimalPoints(animalId);

        var locationIndex = animalVisitedLocations.indexOf(visitedLocation);
        if (locationIndex < 0) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatDoesNotContainError(animal, visitedLocation, "visitedLocations")
            );
        }

        var point = locationRepository.findById(location.getLocationPointId()).orElse(null);
        if (point == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(
                    Location.class, location.getLocationPointId()
                )
            );
        }

        if (locationIndex == 0 && point.equals(animal.getChippingLocation())) {
            throw new IntegrityBreachException(FIRST_ITEM_EQUALS_CHIPPING_LOCATION + animal);
        }

        if (point.equals(animalVisitedLocations.get(locationIndex).getVisitedLocation())) {
            throw new IntegrityBreachException(
                ErrorDetailsFormatter.formatAlreadyContainsError(animal, visitedLocation, "visitedLocations")
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

        visitedLocation.setVisitedLocation(point);
        visitedLocation.setVisitedAt(OffsetDateTime.now());

        return AnimalLocationSchema.createFromModel(repository.save(visitedLocation));
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
                ErrorDetailsFormatter.formatNotFoundError(AnimalVisitedLocation.class, locationId)
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
