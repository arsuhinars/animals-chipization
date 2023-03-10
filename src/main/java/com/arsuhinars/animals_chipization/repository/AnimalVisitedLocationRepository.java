package com.arsuhinars.animals_chipization.repository;

import com.arsuhinars.animals_chipization.model.AnimalVisitedLocation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AnimalVisitedLocationRepository extends CrudRepository<AnimalVisitedLocation, Long> {
    @Query("""
        SELECT * FROM AnimalVisitedLocation a WHERE (
            a.animal.id = %1
            (%2 IS NULL OR %2 >= a.visitedAt) AND
            (%3 IS NULL OR %3 <= a.visitedAt)
        )
        """)
    List<AnimalVisitedLocation> search(
        Long animalId,
        LocalDateTime start,
        LocalDateTime end,
        Pageable pageable
    );

    @Query("""
        SELECT * FROM AnimalVisitedLocation a
        WHERE a.animal.id = %1
        ORDER BY a.visitedAt DESC LIMIT 1
        """)
    Optional<AnimalVisitedLocation> getAnimalLastPoint(Long animalId);

    @Query("""
        SELECT * FROM AnimalVisitedLocation a
        WHERE a.animal.id = %1
        ORDER BY a.visitedAt ASC
        """)
    List<AnimalVisitedLocation> getSortedAnimalPoints(Long animalId);
}
