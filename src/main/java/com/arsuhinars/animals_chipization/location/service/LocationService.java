package com.arsuhinars.animals_chipization.location.service;

import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.DependsOnException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.core.util.GeoPosition;
import com.arsuhinars.animals_chipization.core.util.Rect;
import com.arsuhinars.animals_chipization.location.model.Location;
import com.arsuhinars.animals_chipization.location.schema.LocationCreateSchema;
import com.arsuhinars.animals_chipization.location.schema.LocationUpdateSchema;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    Location create(LocationCreateSchema schema) throws AlreadyExistException;

    Optional<Location> getById(Long id);

    Long getId(GeoPosition position) throws NotFoundException;

    List<Location> getInRange(Rect range);

    Location update(Long id, LocationUpdateSchema schema) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, DependsOnException;
}
