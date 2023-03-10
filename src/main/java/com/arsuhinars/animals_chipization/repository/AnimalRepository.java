package com.arsuhinars.animals_chipization.repository;

import com.arsuhinars.animals_chipization.model.Animal;
import com.arsuhinars.animals_chipization.model.AnimalGender;
import com.arsuhinars.animals_chipization.model.LifeStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends CrudRepository<Animal, Long> {
    Optional<Animal> findByChipperId(Long chipperId);

    boolean existsByChipperId(Long chipperId);

    boolean existsByTypesId(Long typeId);

    @Query("""
        SELECT * FROM Animal a WHERE (
            (%1 IS NULL OR %1 >= a.chippingDateTime) AND
            (%2 IS NULL OR %2 <= a.chippingDateTime) AND
            (%3 IS NULL OR %3 = a.chipper.id) AND
            (%4 IS NULL OR %4 = a.chippingLocation.id) AND
            (%5 IS NULL OR %5 = a.lifeStatus) AND
            (%6 IS NULL OR %6 = a.gender)
        )
        """)
    List<Animal> search(
        LocalDateTime start,
        LocalDateTime end,
        Long chipperId,
        Long chippingLocationId,
        LifeStatus lifeStatus,
        AnimalGender gender,
        Pageable pageable
    );
}
