package com.arsuhinars.animals_chipization.controller;

import com.arsuhinars.animals_chipization.exception.IntegrityBreachException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationSchema;
import com.arsuhinars.animals_chipization.schema.animal.location.AnimalLocationUpdateSchema;
import com.arsuhinars.animals_chipization.service.AnimalLocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/animals")
@Validated
public class AnimalLocationController {
    @Autowired
    private AnimalLocationService service;

    @GetMapping("/{animalId}/locations")
    public List<AnimalLocationSchema> getAnimalLocations(
        @PathVariable @Min(1) Long animalId,
        @RequestParam(required = false) OffsetDateTime startDateTime,
        @RequestParam(required = false) OffsetDateTime endDateTime,
        @RequestParam(defaultValue = "0") Integer from,
        @RequestParam(defaultValue = "10") Integer size
    ) throws NotFoundException {
        return service.search(animalId, startDateTime, endDateTime, from, size);
    }

    @PostMapping("/{animalId}/locations/{pointId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalLocationSchema createAnimalLocation(
        @PathVariable @Min(1) Long animalId,
        @PathVariable @Min(1) Long pointId
    ) throws NotFoundException, IntegrityBreachException {
        return service.create(animalId, pointId);
    }

    @PutMapping("/{animalId}/locations")
    public AnimalLocationSchema updateAnimalLocation(
        @PathVariable @Min(1) Long animalId,
        @Valid @RequestBody AnimalLocationUpdateSchema location
    ) throws NotFoundException, IntegrityBreachException {
        return service.update(animalId, location);
    }

    @DeleteMapping("/{animalId}/locations/{visitedPointId}")
    public void deleteAnimalLocation(
        @PathVariable @Min(1) Long animalId,
        @PathVariable @Min(1) Long visitedPointId
    ) throws NotFoundException {
        service.delete(animalId, visitedPointId);
    }
}
