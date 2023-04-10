package com.arsuhinars.animals_chipization.animal.controller;

import com.arsuhinars.animals_chipization.core.exception.*;
import com.arsuhinars.animals_chipization.animal.enums.Gender;
import com.arsuhinars.animals_chipization.animal.enums.LifeStatus;
import com.arsuhinars.animals_chipization.animal.model.Animal;
import com.arsuhinars.animals_chipization.animal.schema.animal.AnimalCreateSchema;
import com.arsuhinars.animals_chipization.animal.schema.animal.AnimalSchema;
import com.arsuhinars.animals_chipization.animal.schema.animal.AnimalChangeUpdateSchema;
import com.arsuhinars.animals_chipization.animal.schema.animal.AnimalUpdateSchema;
import com.arsuhinars.animals_chipization.animal.service.AnimalService;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/animals")
@Validated
public class AnimalController {
    private final AnimalService service;

    public AnimalController(AnimalService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public AnimalSchema getAnimalById(@PathVariable @Min(1) Long id) throws NotFoundException {
        var animal = service.getById(id);
        if (animal.isEmpty()) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Animal.class, id)
            );
        }

        return animal.map(AnimalSchema::new).get();
    }

    @GetMapping("/search")
    public List<AnimalSchema> searchAnimals(
        @RequestParam(required = false) OffsetDateTime startDateTime,
        @RequestParam(required = false) OffsetDateTime endDateTime,
        @RequestParam(required = false) Long chipperId,
        @RequestParam(required = false) Long chippingLocationId,
        @RequestParam(required = false) LifeStatus lifeStatus,
        @RequestParam(required = false) Gender gender,
        @RequestParam(defaultValue = "0") @Min(0) Integer from,
        @RequestParam(defaultValue = "10") @Min(1) Integer size
    ) {
        return service.search(
            startDateTime,
            endDateTime,
            chipperId,
            chippingLocationId,
            lifeStatus,
            gender,
            from,
            size
        ).stream().map(AnimalSchema::new).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalSchema createAnimal(
        @Valid @RequestBody AnimalCreateSchema animal
    ) throws NotFoundException, AlreadyExistException {
        return new AnimalSchema(service.create(animal));
    }

    @PutMapping("/{id}")
    public AnimalSchema updateAnimalById(
        @PathVariable @Min(1) Long id,
        @Valid @RequestBody AnimalUpdateSchema animal
    ) throws NotFoundException, IntegrityBreachException {
        return new AnimalSchema(service.update(id, animal));
    }

    @DeleteMapping("/{id}")
    public void deleteAnimalById(@PathVariable @Min(1) Long id) throws NotFoundException, DependsOnException {
        service.delete(id);
    }

    @PostMapping("/{id}/types/{typeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalSchema addAnimalType(
        @PathVariable @Min(1) Long id,
        @PathVariable @Min(1) Long typeId
    ) throws NotFoundException, AlreadyExistException {
        return new AnimalSchema(service.addType(id, typeId));
    }

    @PutMapping("/{id}/types")
    public AnimalSchema updateAnimalType(
        @PathVariable @Min(1) Long id,
        @Valid @RequestBody AnimalChangeUpdateSchema schema
    ) throws NotFoundException, AlreadyExistException {
        return new AnimalSchema(
            service.updateType(id, schema.getOldTypeId(), schema.getNewTypeId())
        );
    }

    @DeleteMapping("/{id}/types/{typeId}")
    public AnimalSchema deleteAnimalType(
        @PathVariable @Min(1) Long id,
        @PathVariable @Min(1) Long typeId
    ) throws NotFoundException, LastItemException {
        return new AnimalSchema(service.deleteType(id, typeId));
    }
}
