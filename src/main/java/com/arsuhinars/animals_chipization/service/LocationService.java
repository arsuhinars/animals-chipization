package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.DependsOnException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.LocationSchema;

public interface LocationService {
    LocationSchema create(LocationSchema location) throws AlreadyExistException;

    LocationSchema getById(Long id);

    LocationSchema update(Long id, LocationSchema location) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, DependsOnException;
}
