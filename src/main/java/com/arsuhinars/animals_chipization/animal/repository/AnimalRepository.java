package com.arsuhinars.animals_chipization.animal.repository;

import com.arsuhinars.animals_chipization.animal.model.Animal;
import com.arsuhinars.animals_chipization.animal.enums.Gender;
import com.arsuhinars.animals_chipization.animal.enums.LifeStatus;
import com.arsuhinars.animals_chipization.area.model.Area;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends CrudRepository<Animal, Long> {
    Optional<Animal> findByChipperId(Long chipperId);

    boolean existsByChipperId(Long chipperId);

    boolean existsByTypesId(Long typeId);

    @Query("""
        SELECT a FROM Animal a WHERE (
            (CAST(?1 AS TIMESTAMP) IS NULL OR a.chippingDateTime >= CAST(?1 AS TIMESTAMP)) AND
            (CAST(?2 AS TIMESTAMP) IS NULL OR a.chippingDateTime <= CAST(?2 AS TIMESTAMP)) AND
            (?3 IS NULL OR ?3 = a.chipper.id) AND
            (?4 IS NULL OR ?4 = a.chippingLocation.id) AND
            (?5 IS NULL OR ?5 = a.lifeStatus) AND
            (?6 IS NULL OR ?6 = a.gender)
        )
        """)
    Page<Animal> search(
        @Nullable OffsetDateTime start,
        @Nullable OffsetDateTime end,
        @Nullable Long chipperId,
        @Nullable Long chippingLocationId,
        @Nullable LifeStatus lifeStatus,
        @Nullable Gender gender,
        Pageable pageable
    );

    @Query("""
        SELECT a.animal FROM AnimalLocation a JOIN a.visitedLocation loc JOIN loc.areas area
        WHERE
            area = ?1 AND
            (CAST(?2 AS TIMESTAMP) IS NULL OR a.visitedAt >= CAST(?2 AS TIMESTAMP)) AND
            (CAST(?3 AS TIMESTAMP) IS NULL OR a.visitedAt <= CAST(?3 AS TIMESTAMP))
        """)
    List<Animal> findVisitedArea(
        Area area, @Nullable OffsetDateTime start, @Nullable OffsetDateTime end
    );

    @Query("""
        SELECT a FROM Animal a JOIN a.chippingLocation loc JOIN loc.areas area
        WHERE
            area = ?1 AND
            (CAST(?2 AS TIMESTAMP) IS NULL OR a.chippingDateTime >= CAST(?2 AS TIMESTAMP)) AND
            (CAST(?3 AS TIMESTAMP) IS NULL OR a.chippingDateTime <= CAST(?3 AS TIMESTAMP))
        """)
    List<Animal> findChippedInArea(
        Area area, @Nullable OffsetDateTime start, @Nullable OffsetDateTime end
    );
}
