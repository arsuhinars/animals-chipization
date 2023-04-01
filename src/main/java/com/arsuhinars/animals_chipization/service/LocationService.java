package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.DependsOnException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.LocationSchema;

import java.util.Optional;

public interface LocationService {
    LocationSchema create(LocationSchema schema) throws AlreadyExistException;

    Optional<LocationSchema> getById(Long id);

    LocationSchema update(Long id, LocationSchema schema) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, DependsOnException;
}
