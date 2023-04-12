package com.arsuhinars.animals_chipization.location.controller;

import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.DependsOnException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.core.util.GeoPosition;
import com.arsuhinars.animals_chipization.location.model.Location;
import com.arsuhinars.animals_chipization.location.schema.LocationCreateSchema;
import com.arsuhinars.animals_chipization.location.schema.LocationSchema;
import com.arsuhinars.animals_chipization.location.schema.LocationUpdateSchema;
import com.arsuhinars.animals_chipization.location.service.LocationService;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
@Validated
public class LocationController {
    private final LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public LocationSchema getLocationById(@PathVariable @Min(1) Long id) throws NotFoundException {
        var location = service.getById(id);
        if (location.isEmpty()) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Location.class, id)
            );
        }

        return location.map(LocationSchema::new).get();
    }

    @GetMapping
    public Long getLocationId(@Valid GeoPosition position) throws NotFoundException {
        return service.getId(position);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationSchema createLocation(
        @Valid @RequestBody LocationCreateSchema location
    ) throws AlreadyExistException {
        return new LocationSchema(service.create(location));
    }

    @PutMapping("/{id}")
    public LocationSchema updateLocationById(
        @PathVariable @Min(1) Long id,
        @Valid @RequestBody LocationUpdateSchema location
    ) throws NotFoundException, AlreadyExistException {
        return new LocationSchema(service.update(id, location));
    }

    @DeleteMapping("/{id}")
    public void deleteLocationById(
        @PathVariable @Min(1) Long id
    ) throws NotFoundException, DependsOnException {
        service.delete(id);
    }
}
