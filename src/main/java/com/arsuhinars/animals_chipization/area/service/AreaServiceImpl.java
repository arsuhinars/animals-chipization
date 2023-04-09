package com.arsuhinars.animals_chipization.area.service;

import com.arsuhinars.animals_chipization.area.model.Area;
import com.arsuhinars.animals_chipization.area.model.AreaPoint;
import com.arsuhinars.animals_chipization.area.repository.AreaRepository;
import com.arsuhinars.animals_chipization.area.schema.AreaCreateSchema;
import com.arsuhinars.animals_chipization.area.schema.AreaPointSchema;
import com.arsuhinars.animals_chipization.area.schema.AreaSchema;
import com.arsuhinars.animals_chipization.area.schema.AreaUpdateSchema;
import com.arsuhinars.animals_chipization.area.util.OptimizedArea;
import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.InvalidFormatException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AreaServiceImpl implements AreaService {
    private final AreaRepository repository;
    private final OptimizedAreaFactory optimizedAreaFactory;
    private final OptimizedAreaService optimizedAreaService;

    public AreaServiceImpl(
        AreaRepository repository,
        OptimizedAreaFactory optimizedAreaFactory,
        OptimizedAreaService optimizedAreaService
    ) {
        this.repository = repository;
        this.optimizedAreaFactory = optimizedAreaFactory;
        this.optimizedAreaService = optimizedAreaService;
    }

    @Override
    public AreaSchema create(
        AreaCreateSchema schema
    ) throws InvalidFormatException, AlreadyExistException {
        if (repository.existsByName(schema.getName())) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(Area.class, "name", schema.getName())
            );
        }

        var optimizedArea = optimizedAreaFactory.createArea(
            schema.getAreaPoints().stream().map(AreaPointSchema::toVector2d).toList()
        );

        var checkResult = checkArea(-1L, optimizedArea);
        if (checkResult != AreaCheckResult.NONE) {
            switch (checkResult) {
                case EXIST -> throw new AlreadyExistException("Area with given points already exist");
                case OVERLAPS -> throw new InvalidFormatException("Area overlaps with other one");
            }
        }

        var area = repository.save(new Area(
            schema.getName(),
            schema.getAreaPoints().stream().map(AreaPoint::new).collect(Collectors.toCollection(ArrayList::new)),
            optimizedArea.getRect()
        ));

        optimizedAreaService.addOrUpdate(area.getId(), optimizedArea);

        return new AreaSchema(area);
    }

    @Override
    public Optional<AreaSchema> getById(Long id) {
        return repository.findById(id).map(AreaSchema::new);
    }

    @Override
    public AreaSchema update(
        Long id, AreaUpdateSchema schema
    ) throws InvalidFormatException, NotFoundException, AlreadyExistException {
        var area = repository.findById(id).orElse(null);
        if (area == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Area.class, id)
            );
        }

        if (!area.getName().equals(schema.getName()) &&
            repository.existsByName(schema.getName())
        ) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(Area.class, "name", schema.getName())
            );
        }

        var optimizedArea = optimizedAreaFactory.createArea(
            schema.getAreaPoints().stream().map(AreaPointSchema::toVector2d).toList()
        );

        var checkResult = checkArea(id, optimizedArea);
        if (checkResult != AreaCheckResult.NONE) {
            switch (checkResult) {
                case EXIST -> throw new AlreadyExistException("Area with given points already exist");
                case OVERLAPS -> throw new InvalidFormatException("Area overlaps with other one");
            }
        }

        optimizedAreaService.addOrUpdate(area.getId(), optimizedArea);

        area.setName(area.getName());
        area.setPoints(
            schema.getAreaPoints().stream().map(AreaPoint::new).collect(Collectors.toCollection(ArrayList::new))
        );
        area.setRect(optimizedArea.getRect());

        return new AreaSchema(repository.save(area));
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        var area = repository.findById(id).orElse(null);
        if (area == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Area.class, id)
            );
        }

        repository.delete(area);
    }

    private AreaCheckResult checkArea(Long id, OptimizedArea area) {
        var overlaps = false;

        var areas = repository.findInRectRange(
            area.getRect().getMinX(),
            area.getRect().getMinY(),
            area.getRect().getMaxX(),
            area.getRect().getMaxY()
        );
        for (var a : areas) {
            if (a.getId().equals(id)) {
                continue;
            }

            var optimizedArea = optimizedAreaService.getById(a.getId());
            if (!optimizedArea.overlapArea(area)) {
                continue;
            }

            if (optimizedArea.equalsTo(area)) {
                return AreaCheckResult.EXIST;
            }

            overlaps = true;
        }

        return overlaps ? AreaCheckResult.OVERLAPS : AreaCheckResult.NONE;
    }

    private enum AreaCheckResult {
        NONE, OVERLAPS, EXIST
    }
}
