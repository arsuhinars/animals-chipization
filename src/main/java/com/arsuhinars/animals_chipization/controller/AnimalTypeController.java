package com.arsuhinars.animals_chipization.controller;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.BoundException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.animal.type.AnimalTypeSchema;
import com.arsuhinars.animals_chipization.service.AnimalTypeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animals/types")
@Validated
public class AnimalTypeController {
    @Autowired
    private AnimalTypeService service;

    @GetMapping("/{id}")
    public AnimalTypeSchema getAnimalTypeById(
        @PathVariable @Min(1) Long id
    ) throws NotFoundException {
        var animalType = service.getById(id);
        if (animalType == null) {
            throw new NotFoundException("Animal type with id " + id + "was not found");
        }

        return animalType;
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
    public void deleteAnimalTypeById(@PathVariable @Min(1) Long id) throws NotFoundException, BoundException {
        service.delete(id);
    }
}
