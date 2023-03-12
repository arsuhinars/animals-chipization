package com.arsuhinars.animals_chipization.controller;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.DependsOnException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.LocationSchema;
import com.arsuhinars.animals_chipization.service.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
@Validated
public class LocationController {
    @Autowired
    private LocationService service;

    @GetMapping("/{id}")
    public LocationSchema getLocationById(@PathVariable @Min(1) Long id) throws NotFoundException {
        var location = service.getById(id);
        if (location == null) {
            throw new NotFoundException("Location with id " + id + " was not found");
        }

        return location;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationSchema createLocation(
        @Valid @RequestBody LocationSchema location
    ) throws AlreadyExistException {
        return service.create(location);
    }

    @PutMapping("/{id}")
    public LocationSchema updateLocationById(
        @PathVariable @Min(1) Long id,
        @Valid @RequestBody LocationSchema location
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
