package com.arsuhinars.animals_chipization.location.repository;

import com.arsuhinars.animals_chipization.location.model.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long> {
    boolean existsByLatitudeAndLongitude(Double latitude, Double longitude);
}
