package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.DependsOnException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.location.LocationCreateSchema;
import com.arsuhinars.animals_chipization.schema.location.LocationSchema;
import com.arsuhinars.animals_chipization.schema.location.LocationUpdateSchema;

import java.util.Optional;

public interface LocationService {
    LocationSchema create(LocationCreateSchema schema) throws AlreadyExistException;

    Optional<LocationSchema> getById(Long id);

    LocationSchema update(Long id, LocationUpdateSchema schema) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, DependsOnException;
}
