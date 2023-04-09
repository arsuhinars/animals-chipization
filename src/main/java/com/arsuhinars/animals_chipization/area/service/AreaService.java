package com.arsuhinars.animals_chipization.area.service;

import com.arsuhinars.animals_chipization.area.schema.AreaCreateSchema;
import com.arsuhinars.animals_chipization.area.schema.AreaSchema;
import com.arsuhinars.animals_chipization.area.schema.AreaUpdateSchema;
import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.InvalidFormatException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;

import java.util.Optional;

public interface AreaService {
    AreaSchema create(AreaCreateSchema schema) throws InvalidFormatException, AlreadyExistException;

    Optional<AreaSchema> getById(Long id);

    AreaSchema update(
        Long id, AreaUpdateSchema schema
    ) throws InvalidFormatException, NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException;
}
