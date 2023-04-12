package com.arsuhinars.animals_chipization.location.service;

import com.arsuhinars.animals_chipization.area.service.AreaService;
import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.DependsOnException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.animal.model.Animal;
import com.arsuhinars.animals_chipization.core.util.GeoPosition;
import com.arsuhinars.animals_chipization.core.util.Rect;
import com.arsuhinars.animals_chipization.location.model.Location;
import com.arsuhinars.animals_chipization.location.repository.LocationRepository;
import com.arsuhinars.animals_chipization.location.schema.LocationCreateSchema;
import com.arsuhinars.animals_chipization.location.schema.LocationUpdateSchema;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository repository;
    private AreaService areaService;

    public LocationServiceImpl(
        LocationRepository repository
    ) {
        this.repository = repository;
    }

    @Autowired
    public void setAreaService(AreaService areaService) {
        this.areaService = areaService;
    }

    @Override
    public Location create(LocationCreateSchema schema) throws AlreadyExistException {
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
            new GeoPosition(schema.getLatitude(), schema.getLongitude())
        );

        areaService.getInPoint(location.getPosition()).ifPresent(location::setArea);

        return repository.save(location);
    }

    @Override
    public Optional<Location> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Location> getInRange(Rect range) {
        return repository.findInRange(range.getMinX(), range.getMinY(), range.getMaxX(), range.getMaxY());
    }

    @Override
    public Location update(Long id, LocationUpdateSchema schema) throws NotFoundException, AlreadyExistException {
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

        location.setPosition(
            new GeoPosition(schema.getLatitude(), schema.getLongitude())
        );

        areaService.getInPoint(location.getPosition()).ifPresent(location::setArea);

        return repository.save(location);
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
