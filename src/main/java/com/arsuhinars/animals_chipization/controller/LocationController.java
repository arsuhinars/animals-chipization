package com.arsuhinars.animals_chipization.controller;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.DependsOnException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.model.Location;
import com.arsuhinars.animals_chipization.schema.location.LocationCreateSchema;
import com.arsuhinars.animals_chipization.schema.location.LocationSchema;
import com.arsuhinars.animals_chipization.schema.location.LocationUpdateSchema;
import com.arsuhinars.animals_chipization.service.LocationService;
import com.arsuhinars.animals_chipization.util.ErrorDetailsFormatter;
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

        return location.get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationSchema createLocation(
        @Valid @RequestBody LocationCreateSchema location
    ) throws AlreadyExistException {
        return service.create(location);
    }

    @PutMapping("/{id}")
    public LocationSchema updateLocationById(
        @PathVariable @Min(1) Long id,
        @Valid @RequestBody LocationUpdateSchema location
    ) throws NotFoundException, AlreadyExistException {
        return service.update(id, location);
    }

    @DeleteMapping("/{id}")
    public void deleteLocationById(
        @PathVariable @Min(1) Long id
    ) throws NotFoundException, DependsOnException {
        service.delete(id);
    }
}
