package com.arsuhinars.animals_chipization.area.service;

import com.arsuhinars.animals_chipization.area.model.Area;
import com.arsuhinars.animals_chipization.area.schema.AreaAnalyticsSchema;
import com.arsuhinars.animals_chipization.area.schema.AreaCreateSchema;
import com.arsuhinars.animals_chipization.area.schema.AreaUpdateSchema;
import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.InvalidFormatException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.core.util.GeoPosition;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AreaService {
    Area create(AreaCreateSchema schema) throws InvalidFormatException, AlreadyExistException;

    Optional<Area> getById(Long id);

    List<Area> getInPoint(GeoPosition point);

    AreaAnalyticsSchema getAnalytics(Long areaId, LocalDate start, LocalDate end) throws NotFoundException;

    Area update(
        Long id, AreaUpdateSchema schema
    ) throws InvalidFormatException, NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException;
}
