package com.arsuhinars.animals_chipization.animal.controller;

import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.DependsOnException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.animal.model.AnimalType;
import com.arsuhinars.animals_chipization.animal.schema.animal_type.AnimalTypeCreateSchema;
import com.arsuhinars.animals_chipization.animal.schema.animal_type.AnimalTypeSchema;
import com.arsuhinars.animals_chipization.animal.schema.animal_type.AnimalTypeUpdateSchema;
import com.arsuhinars.animals_chipization.animal.service.AnimalTypeService;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
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

        return animalType.map(AnimalTypeSchema::new).get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalTypeSchema createAnimalType(
        @Valid @RequestBody AnimalTypeCreateSchema animalType
    ) throws AlreadyExistException {
        return new AnimalTypeSchema(service.create(animalType));
    }

    @PutMapping("/{id}")
    public AnimalTypeSchema updateAnimalTypeById(
        @PathVariable @Min(1) Long id,
        @Valid @RequestBody AnimalTypeUpdateSchema animalType
    ) throws NotFoundException, AlreadyExistException {
        return new AnimalTypeSchema(service.update(id, animalType));
    }

    @DeleteMapping("/{id}")
    public void deleteAnimalTypeById(@PathVariable @Min(1) Long id) throws NotFoundException, DependsOnException {
        service.delete(id);
    }
}
