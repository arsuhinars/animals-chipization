package com.arsuhinars.animals_chipization.area.service;

import com.arsuhinars.animals_chipization.animal.model.Animal;
import com.arsuhinars.animals_chipization.animal.model.AnimalLocation;
import com.arsuhinars.animals_chipization.animal.model.AnimalType;
import com.arsuhinars.animals_chipization.animal.repository.AnimalRepository;
import com.arsuhinars.animals_chipization.area.model.Area;
import com.arsuhinars.animals_chipization.area.repository.AreaRepository;
import com.arsuhinars.animals_chipization.area.schema.*;
import com.arsuhinars.animals_chipization.area.util.OptimizedArea;
import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.InvalidFormatException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import com.arsuhinars.animals_chipization.core.util.GeoPosition;
import com.arsuhinars.animals_chipization.location.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class AreaServiceImpl implements AreaService {
    private final AreaRepository repository;
    private final OptimizedAreaFactory optimizedAreaFactory;
    private final OptimizedAreaService optimizedAreaService;
    private final AnimalRepository animalRepository;
    private final LocationRepository locationRepository;

    public AreaServiceImpl(
        AreaRepository repository,
        OptimizedAreaFactory optimizedAreaFactory,
        OptimizedAreaService optimizedAreaService,
        AnimalRepository animalRepository,
        LocationRepository locationRepository

    ) {
        this.repository = repository;
        this.optimizedAreaFactory = optimizedAreaFactory;
        this.optimizedAreaService = optimizedAreaService;
        this.animalRepository = animalRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public Area create(
        AreaCreateSchema schema
    ) throws InvalidFormatException, AlreadyExistException {
        if (repository.existsByName(schema.getName())) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(Area.class, "name", schema.getName())
            );
        }

        var optimizedArea = optimizedAreaFactory.createArea(
            schema.getAreaPoints().stream().map(GeoPosition::toVector2d).toList()
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
            new ArrayList<>(schema.getAreaPoints()),
            optimizedArea.getRect()
        ));
        optimizedAreaService.addOrUpdate(area.getId(), optimizedArea);
        applyAreaToLocations(area);

        return area;
    }

    @Override
    public Optional<Area> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Area> getInPoint(GeoPosition point) {
        var areas = repository.findInPointRange(point.getLatitude(), point.getLongitude());
        var result = new LinkedList<Area>();

        for (var area : areas) {
            var optimizedArea = optimizedAreaService.getById(area.getId());
            if (optimizedArea.containsPoint(point.toVector2d())) {
                result.add(area);
            }
        }

        return result;
    }

    @Override
    public AreaAnalyticsSchema getAnalytics(
        Long areaId, LocalDate start, LocalDate end
    ) throws NotFoundException {
        var area = repository.findById(areaId).orElse(null);
        if (area == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Area.class, areaId)
            );
        }

        var startAsDateTime = OffsetDateTime.of(start, LocalTime.MIN, ZoneOffset.UTC);
        var endAsDateTime = OffsetDateTime.of(end, LocalTime.MAX, ZoneOffset.UTC);

        var animals = new HashSet<Animal>();
        animals.addAll(animalRepository.findVisitedArea(area, startAsDateTime, endAsDateTime));
        animals.addAll(animalRepository.findChippedInArea(area, startAsDateTime, endAsDateTime));

        var locationsComparator = Comparator.comparing(
            AnimalLocation::getVisitedAt, OffsetDateTime::compareTo
        ).thenComparingLong(AnimalLocation::getId);

        var analytics = new AreaAnalyticsSchema();
        var animalsAnalytics = new HashMap<AnimalType, AnimalsAnalytics>();

        for (var animal : animals) {
            animal.getTypes().forEach(type -> animalsAnalytics.computeIfAbsent(type, AnimalsAnalytics::new));

            var locations = new ArrayList<>(animal.getVisitedLocations());
            locations.add(new AnimalLocation(
                0L,
                animal,
                animal.getChippingLocation(),
                animal.getChippingDateTime()
            ));
            locations.sort(locationsComparator);

            var didArrive = false;
            var didGone = false;
            for (int i = 0; i < locations.size() - 1; ++i) {
                var currDate = locations.get(i).getVisitedAt().toLocalDate();
                var currAreas = locations.get(i).getVisitedLocation().getAreas();
                var nextDate = locations.get(i + 1).getVisitedAt().toLocalDate();
                var nextAreas = locations.get(i + 1).getVisitedLocation().getAreas();

                if (!didArrive &&
                    nextDate.isAfter(start) &&
                    !currAreas.contains(area) &&
                    nextAreas.contains(area)
                ) {
                    didArrive = true;
                }

                if (!didGone &&
                    currDate.isBefore(end) &&
                    currAreas.contains(area) &&
                    !nextAreas.contains(area)
                ) {
                    didGone = true;
                }
            }

            if (!didGone) {
                analytics.increaseQuantity();
                animal.getTypes().forEach(type -> animalsAnalytics.get(type).increaseQuantity());
            }
            if (didArrive) {
                analytics.increaseArrived();
                animal.getTypes().forEach(type -> animalsAnalytics.get(type).increaseArrived());
            }
            if (didGone) {
                analytics.increaseGone();
                animal.getTypes().forEach(type -> animalsAnalytics.get(type).increaseGone());
            }
        }

        analytics.setAnimalsAnalytics(animalsAnalytics.values().stream().toList());

        return analytics;
    }

    @Override
    public Area update(
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
            schema.getAreaPoints().stream().map(GeoPosition::toVector2d).toList()
        );

        var checkResult = checkArea(id, optimizedArea);
        if (checkResult != AreaCheckResult.NONE) {
            switch (checkResult) {
                case EXIST -> throw new AlreadyExistException("Area with given points already exist");
                case OVERLAPS -> throw new InvalidFormatException("Area overlaps with other one");
            }
        }

        area.setName(area.getName());
        area.setPoints(
            new ArrayList<>(schema.getAreaPoints())
        );
        area.setRect(optimizedArea.getRect());
        area = repository.save(area);

        optimizedAreaService.addOrUpdate(area.getId(), optimizedArea);
        applyAreaToLocations(area);

        return area;
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        var area = repository.findById(id).orElse(null);
        if (area == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Area.class, id)
            );
        }

        for (var location : area.getLocations()) {
            location.getAreas().remove(area);
        }
        locationRepository.saveAll(area.getLocations());

        repository.delete(area);
    }

    private void applyAreaToLocations(Area area) {
        var optimizedArea = optimizedAreaService.getById(area.getId());
        var rect = optimizedArea.getRect();

        var locations = locationRepository.findInRange(
            rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY()
        );
        for (var loc : locations) {
            if (optimizedArea.containsPoint(loc.getPosition().toVector2d())) {
                loc.getAreas().add(area);
                locationRepository.save(loc);
            }
        }
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
