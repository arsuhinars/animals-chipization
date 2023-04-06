package com.arsuhinars.animals_chipization.location.service;

import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.DependsOnException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.animal.model.Animal;
import com.arsuhinars.animals_chipization.location.model.Location;
import com.arsuhinars.animals_chipization.location.repository.LocationRepository;
import com.arsuhinars.animals_chipization.location.schema.LocationCreateSchema;
import com.arsuhinars.animals_chipization.location.schema.LocationSchema;
import com.arsuhinars.animals_chipization.location.schema.LocationUpdateSchema;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository repository;

    public LocationServiceImpl(LocationRepository repository) {
        this.repository = repository;
    }

    @Override
    public LocationSchema create(LocationCreateSchema schema) throws AlreadyExistException {
        if (repository.existsByLatitudeAndLongitude(
                schema.getLatitude(),
                schema.getLongitude()
            )
        ) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(
                    Location.class,
                    "[latitude, longitude]",
                    List.of(schema.getLatitude(), schema.getLongitude())
                )
            );
        }

        var location = new Location(
            schema.getLatitude(), schema.getLongitude()
        );

        return new LocationSchema(repository.save(location));
    }

    @Override
    public Optional<LocationSchema> getById(Long id) {
        return repository.findById(id).map(LocationSchema::new);
    }

    @Override
    public LocationSchema update(Long id, LocationUpdateSchema schema) throws NotFoundException, AlreadyExistException {
        var location = repository.findById(id).orElse(null);
        if (location == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Location.class, id)
            );
        }

        if (repository.existsByLatitudeAndLongitude(
            schema.getLatitude(), schema.getLongitude())
        ) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(
                    Location.class,
                    "[latitude, longitude]",
                    List.of(schema.getLatitude(), schema.getLongitude())
                )
            );
        }

        location.setLatitude(schema.getLatitude());
        location.setLongitude(schema.getLongitude());

        return new LocationSchema(repository.save(location));
    }

    @Override
    public void delete(Long id) throws NotFoundException, DependsOnException {
        var location = repository.findById(id).orElse(null);
        if (location == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Location.class, id)
            );
        }

        if (!location.getChippedAnimals().isEmpty() ||
            !location.getVisitedAnimals().isEmpty()
        ) {
            throw new DependsOnException(
                ErrorDetailsFormatter.formatDependsOnError(location, Animal.class)
            );
        }

        repository.delete(location);
    }
}
