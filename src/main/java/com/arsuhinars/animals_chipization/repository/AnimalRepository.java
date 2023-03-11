package com.arsuhinars.animals_chipization.repository;

import com.arsuhinars.animals_chipization.model.Animal;
import com.arsuhinars.animals_chipization.model.AnimalGender;
import com.arsuhinars.animals_chipization.model.LifeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface AnimalRepository extends CrudRepository<Animal, Long> {
    Optional<Animal> findByChipperId(Long chipperId);

    boolean existsByChipperId(Long chipperId);

    boolean existsByTypesId(Long typeId);

    @Query("""
        SELECT a FROM Animal a WHERE (
            (CAST(?1 AS DATE) IS NULL OR CAST(?1 AS DATE) >= a.chippingDateTime) AND
            (CAST(?2 AS DATE) IS NULL OR CAST(?2 AS DATE) <= a.chippingDateTime) AND
            (?3 IS NULL OR ?3 = a.chipper.id) AND
            (?4 IS NULL OR ?4 = a.chippingLocation.id) AND
            (?5 IS NULL OR ?5 = a.lifeStatus) AND
            (?6 IS NULL OR ?6 = a.gender)
        )
        """)
    Page<Animal> search(
        OffsetDateTime start,
        OffsetDateTime end,
        Long chipperId,
        Long chippingLocationId,
        LifeStatus lifeStatus,
        AnimalGender gender,
        Pageable pageable
    );
}
