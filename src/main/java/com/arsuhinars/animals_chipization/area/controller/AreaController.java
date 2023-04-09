package com.arsuhinars.animals_chipization.area.controller;

import com.arsuhinars.animals_chipization.area.model.Area;
import com.arsuhinars.animals_chipization.area.schema.AreaCreateSchema;
import com.arsuhinars.animals_chipization.area.schema.AreaSchema;
import com.arsuhinars.animals_chipization.area.schema.AreaUpdateSchema;
import com.arsuhinars.animals_chipization.area.service.AreaService;
import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/areas")
@Validated
public class AreaController {
    private final AreaService service;

    public AreaController(AreaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public AreaSchema getAreaById(
        @PathVariable @Min(1) Long id
    ) throws NotFoundException {
        var area = service.getById(id);
        if (area.isEmpty()) {
            throw new NotFoundException(ErrorDetailsFormatter.formatNotFoundError(Area.class, id));
        }

        return area.get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AreaSchema createArea(
        @Valid @RequestBody AreaCreateSchema schema
    ) throws AlreadyExistException {
        return service.create(schema);
    }

    @PutMapping("/{id}")
    public AreaSchema updateSchemaById(
        @PathVariable @Min(1) Long id,
        @Valid @RequestBody AreaUpdateSchema schema
    ) throws NotFoundException, AlreadyExistException {
        return service.update(id, schema);
    }

    @DeleteMapping("/{id}")
    public void deleteSchemaById(
        @PathVariable @Min(1) Long id
    ) throws NotFoundException {
        service.delete(id);
    }
}
