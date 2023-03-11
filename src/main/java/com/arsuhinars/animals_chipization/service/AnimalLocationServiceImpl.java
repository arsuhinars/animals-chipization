package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.IntegrityBreachException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.model.AnimalVisitedLocation;
import com.arsuhinars.animals_chipization.model.LifeStatus;
import com.arsuhinars.animals_chipization.repository.AnimalRepository;
import com.arsuhinars.animals_chipization.repository.AnimalVisitedLocationRepository;
import com.arsuhinars.animals_chipization.repository.LocationRepository;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationSchema;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationUpdateSchema;
import com.arsuhinars.animals_chipization.util.OffsetPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class AnimalLocationServiceImpl implements AnimalLocationService {
    @Autowired
    private AnimalVisitedLocationRepository repository;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public AnimalLocationSchema create(
        Long animalId, Long pointId
    ) throws NotFoundException, IntegrityBreachException {
        var animal = animalRepository.findById(animalId);
        if (animal.isEmpty()) {
            throw new NotFoundException("Animal with id " + animalId + " was not found");
        }

        var point = locationRepository.findById(pointId);
        if (point.isEmpty()) {
            throw new NotFoundException("Location with id " + pointId + " was not found");
        }

        if (animal.get().getLifeStatus() == LifeStatus.DEAD) {
            throw new IntegrityBreachException("Unable to add visited point when animal life status is DEAD");
        }

        var animalLastPoint = repository.getAnimalLastPoint(animalId);
        if (animalLastPoint.isPresent() &&
            animalLastPoint.get().getVisitedLocation().getId().equals(pointId)
        ) {
            throw new IntegrityBreachException("Unable to add point where animal is already situated");
        }

        if (animalLastPoint.isEmpty() &&
            pointId.equals(animal.get().getChippingLocation().getId())
        ) {
            throw new IntegrityBreachException("Unable to add point where animal was chipped");
        }

        return AnimalLocationSchema.createFromModel(
            repository.save(new AnimalVisitedLocation(
                animal.get(),
                point.get(),
                OffsetDateTime.now()
            ))
        );
    }

    @Override
    public List<AnimalLocationSchema> search(
        Long animalId, OffsetDateTime start, OffsetDateTime end, int from, int size
    ) throws NotFoundException {
        if (!animalRepository.existsById(animalId)) {
            throw new NotFoundException("Animal with id " + animalId + " was not found");
        }

        return repository.search(
            animalId, start, end, new OffsetPageable(size, from, Sort.by("visitedAt").ascending())
            )
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

        var animalVisitedLocations = animal.get()
            .getVisitedLocations()
            .stream()
            .sorted(Comparator.comparing(AnimalVisitedLocation::getVisitedAt))
            .toList();
        var locationIndex = animalVisitedLocations.indexOf(visitedPoint.get());
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
                animalVisitedLocations.get(locationIndex).getVisitedLocation().getId()
            )
        ) {
            throw new IntegrityBreachException("Unable to change point to the same one");
        }

        if (locationIndex < animalVisitedLocations.size() - 1 &&
            location.getLocationPointId().equals(
                animalVisitedLocations.get(locationIndex + 1).getVisitedLocation().getId()
            )
        ) {
            throw new IntegrityBreachException("The next point is the same");
        }

        if (locationIndex > 0 &&
            location.getLocationPointId().equals(
                animalVisitedLocations.get(locationIndex - 1).getVisitedLocation().getId()
            )
        ) {
            throw new IntegrityBreachException("The previous point is the same");
        }

        var dbPoint = visitedPoint.get();
        dbPoint.setVisitedLocation(point.get());
        dbPoint.setVisitedAt(OffsetDateTime.now());

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

        var animalVisitedLocations = animal.get()
            .getVisitedLocations()
            .stream()
            .sorted(Comparator.comparing(AnimalVisitedLocation::getVisitedAt))
            .toList();
        var locationIndex = animalVisitedLocations.indexOf(visitedPoint.get());
        if (locationIndex < 0) {
            throw new NotFoundException(
                "Animal doesn't have visited location with id " + locationId
            );
        }

        if (locationIndex == 0 &&
            animalVisitedLocations.size() > 1 &&
            animal.get().getChippingLocation().getId().equals(
                animalVisitedLocations.get(locationIndex + 1).getVisitedLocation().getId()
            )
        ) {
            animal.get().getVisitedLocations().remove(animalVisitedLocations.get(1));
        }
        animal.get().getVisitedLocations().remove(visitedPoint.get());

        animalRepository.save(animal.get());
    }
}
