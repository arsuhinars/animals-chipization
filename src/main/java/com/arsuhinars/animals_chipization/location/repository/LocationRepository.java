package com.arsuhinars.animals_chipization.location.repository;

import com.arsuhinars.animals_chipization.location.model.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends CrudRepository<Location, Long> {
    @Query("""
        SELECT
            CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
        FROM Location loc WHERE loc.position.latitude = ?1 AND loc.position.longitude = ?2
        """)
    boolean existsByPosition(Double latitude, Double longitude);

    @Query("""
        SELECT loc FROM Location loc WHERE
            loc.position.latitude = ?1 AND loc.position.longitude = ?2
        """)
    Optional<Location> findByPosition(Double latitude, Double longitude);

    @Query("""
        SELECT loc FROM Location loc WHERE
            loc.position.latitude >= :minX AND loc.position.latitude <= :maxX AND
            loc.position.longitude >= :minY AND loc.position.longitude <= :maxY
        """)
    List<Location> findInRange(double minX, double minY, double maxX, double maxY);
}
