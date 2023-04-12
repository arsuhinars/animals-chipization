package com.arsuhinars.animals_chipization.area.controller;

import com.arsuhinars.animals_chipization.area.model.Area;
import com.arsuhinars.animals_chipization.area.schema.AreaAnalyticsSchema;
import com.arsuhinars.animals_chipization.area.schema.AreaCreateSchema;
import com.arsuhinars.animals_chipization.area.schema.AreaSchema;
import com.arsuhinars.animals_chipization.area.schema.AreaUpdateSchema;
import com.arsuhinars.animals_chipization.area.service.AreaService;
import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.InvalidFormatException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.*;

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

        return area.map(AreaSchema::new).get();
    }

    @GetMapping("/{id}/analytics")
    public AreaAnalyticsSchema getAnalytics(
        @PathVariable @Min(1) Long id,
        @RequestParam @NotNull LocalDate startDate,
        @RequestParam @NotNull LocalDate endDate
    ) throws NotFoundException, InvalidFormatException {
        if (startDate.isEqual(endDate) || startDate.isAfter(endDate)) {
            throw new InvalidFormatException("startDate must be before endDate");
        }

        return service.getAnalytics(
            id,
            OffsetDateTime.of(startDate, LocalTime.MIDNIGHT, ZoneOffset.UTC),
            OffsetDateTime.of(endDate, LocalTime.MIDNIGHT, ZoneOffset.UTC)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AreaSchema createArea(
        @Valid @RequestBody AreaCreateSchema schema
    ) throws AlreadyExistException {
        return new AreaSchema(service.create(schema));
    }

    @PutMapping("/{id}")
    public AreaSchema updateAreaById(
        @PathVariable @Min(1) Long id,
        @Valid @RequestBody AreaUpdateSchema schema
    ) throws NotFoundException, AlreadyExistException {
        return new AreaSchema(service.update(id, schema));
    }

    @DeleteMapping("/{id}")
    public void deleteAreaById(
        @PathVariable @Min(1) Long id
    ) throws NotFoundException {
        service.delete(id);
    }
}
