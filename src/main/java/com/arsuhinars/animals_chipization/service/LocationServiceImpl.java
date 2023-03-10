package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.BoundException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.model.Location;
import com.arsuhinars.animals_chipization.repository.LocationRepository;
import com.arsuhinars.animals_chipization.schema.LocationSchema;
import org.springframework.beans.factory.annotation.Autowired;

public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository repository;

    @Override
    public LocationSchema create(LocationSchema location) throws AlreadyExistException {
        if (repository.existsByLatitudeAndLongitude(
            location.getLatitude(),
            location.getLongitude())
        ) {
            throw new AlreadyExistException("Location with given latitude and longitude already exists");
        }

        return LocationSchema.createFromModel(
            repository.save(new Location(
                location.getLatitude(),
                location.getLongitude()
            ))
        );
    }

    @Override
    public LocationSchema getById(Long id) {
        return repository.findById(id).map(LocationSchema::createFromModel).orElse(null);
    }

    @Override
    public LocationSchema update(Long id, LocationSchema location) throws NotFoundException, AlreadyExistException {
        var result = repository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Location with id " + id + " was not found");
        }

        if (repository.existsByLatitudeAndLongitude(
            location.getLatitude(),
            location.getLongitude())
        ) {
            throw new AlreadyExistException("Location with given latitude and longitude already exists");
        }

        var dbLocation = result.get();
        dbLocation.setLatitude(location.getLatitude());
        dbLocation.setLongitude(location.getLongitude());

        return LocationSchema.createFromModel(repository.save(dbLocation));
    }

    @Override
    public void delete(Long id) throws NotFoundException, BoundException {
        var result = repository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Location with id " + id + " was not found");
        }

        if (!result.get().getChippedAnimals().isEmpty() ||
            !result.get().getVisitedAnimals().isEmpty()
        ) {
            throw new BoundException("Location is bound with some animals");
        }

        repository.deleteById(id);
    }
}
