package com.arsuhinars.animals_chipization.controller;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.DependsOnException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.model.AnimalType;
import com.arsuhinars.animals_chipization.schema.AnimalTypeSchema;
import com.arsuhinars.animals_chipization.service.AnimalTypeService;
import com.arsuhinars.animals_chipization.util.ErrorDetailsFormatter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animals/types")
@Validated
public class AnimalTypeController {
    private final AnimalTypeService service;

    public AnimalTypeController(AnimalTypeService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public AnimalTypeSchema getAnimalTypeById(
        @PathVariable @Min(1) Long id
    ) throws NotFoundException {
        var animalType = service.getById(id);
        if (animalType.isEmpty()) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(AnimalType.class, id)
            );
        }

        return animalType.get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalTypeSchema createAnimalType(
        @Valid @RequestBody AnimalTypeSchema animalType
    ) throws AlreadyExistException {
        return service.create(animalType);
    }

    @PutMapping("/{id}")
    public AnimalTypeSchema updateAnimalTypeById(
        @PathVariable @Min(1) Long id,
        @Valid @RequestBody AnimalTypeSchema animalType
    ) throws NotFoundException, AlreadyExistException {
        return service.update(id, animalType);
    }

    @DeleteMapping("/{id}")
    public void deleteAnimalTypeById(@PathVariable @Min(1) Long id) throws NotFoundException, DependsOnException {
        service.delete(id);
    }
}
