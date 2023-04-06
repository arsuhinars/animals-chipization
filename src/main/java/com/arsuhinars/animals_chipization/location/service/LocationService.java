package com.arsuhinars.animals_chipization.location.service;

import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.DependsOnException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.location.schema.LocationCreateSchema;
import com.arsuhinars.animals_chipization.location.schema.LocationSchema;
import com.arsuhinars.animals_chipization.location.schema.LocationUpdateSchema;

import java.util.Optional;

public interface LocationService {
    LocationSchema create(LocationCreateSchema schema) throws AlreadyExistException;

    Optional<LocationSchema> getById(Long id);

    LocationSchema update(Long id, LocationUpdateSchema schema) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, DependsOnException;
}
