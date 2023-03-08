package com.arsuhinars.animals_chipization.repository;

import com.arsuhinars.animals_chipization.model.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long> {
    boolean existsByLatitudeAndLongitude(Double latitude, Double longitude);
}
