package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.DependsOnException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.model.Animal;
import com.arsuhinars.animals_chipization.model.Location;
import com.arsuhinars.animals_chipization.repository.LocationRepository;
import com.arsuhinars.animals_chipization.schema.LocationSchema;
import com.arsuhinars.animals_chipization.util.ErrorDetailsFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository repository;

    @Override
    public LocationSchema create(LocationSchema location) throws AlreadyExistException {
        if (repository.existsByLatitudeAndLongitude(
                location.getLatitude(),
                location.getLongitude()
            )
        ) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(
                    Location.class,
                    "[latitude, longitude]",
                    List.of(location.getLatitude(), location.getLongitude())
                )
            );
        }

        var dbLocation = new Location(
            location.getLatitude(),
            location.getLongitude()
        );

        return LocationSchema.createFromModel(repository.save(dbLocation));
    }

    @Override
    public LocationSchema getById(Long id) {
        return repository.findById(id).map(LocationSchema::createFromModel).orElse(null);
    }

    @Override
    public LocationSchema update(Long id, LocationSchema location) throws NotFoundException, AlreadyExistException {
        var dbLocation = repository.findById(id).orElse(null);
        if (dbLocation == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Location.class, id)
            );
        }

        if (repository.existsByLatitudeAndLongitude(
                location.getLatitude(),
                location.getLongitude()
            )
        ) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(
                    Location.class,
                    "[latitude, longitude]",
                    List.of(location.getLatitude(), location.getLongitude())
                )
            );
        }

        dbLocation.setLatitude(location.getLatitude());
        dbLocation.setLongitude(location.getLongitude());

        return LocationSchema.createFromModel(repository.save(dbLocation));
    }

    @Override
    public void delete(Long id) throws NotFoundException, DependsOnException {
        var dbLocation = repository.findById(id).orElse(null);
        if (dbLocation == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Location.class, id)
            );
        }

        if (!dbLocation.getChippedAnimals().isEmpty() ||
            !dbLocation.getVisitedAnimals().isEmpty()
        ) {
            throw new DependsOnException(
                ErrorDetailsFormatter.formatDependsOnError(dbLocation, Animal.class)
            );
        }

        repository.delete(dbLocation);
    }
}
