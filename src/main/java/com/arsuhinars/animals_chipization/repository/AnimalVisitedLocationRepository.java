package com.arsuhinars.animals_chipization.repository;

import com.arsuhinars.animals_chipization.model.AnimalVisitedLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface AnimalVisitedLocationRepository extends CrudRepository<AnimalVisitedLocation, Long> {
    @Query("""
        SELECT loc FROM AnimalVisitedLocation loc JOIN loc.animal a
        WHERE
            a.id = ?1 AND
            (CAST(?2 AS DATE) IS NULL OR CAST(?2 AS DATE) >= loc.visitedAt) AND
            (CAST(?3 AS DATE) IS NULL OR CAST(?3 AS DATE) <= loc.visitedAt)
        """)
    Page<AnimalVisitedLocation> search(
        Long animalId,
        OffsetDateTime start,
        OffsetDateTime end,
        Pageable pageable
    );

    @Query("""
        SELECT loc FROM AnimalVisitedLocation loc JOIN loc.animal a
        WHERE a.id = ?1
        ORDER BY loc.visitedAt ASC LIMIT 1
        """)
    Optional<AnimalVisitedLocation> getAnimalFirstPoint(Long animalId);

    @Query("""
        SELECT loc FROM AnimalVisitedLocation loc JOIN loc.animal a
        WHERE a.id = ?1
        ORDER BY loc.visitedAt DESC LIMIT 1
        """)
    Optional<AnimalVisitedLocation> getAnimalLastPoint(Long animalId);

    @Query("""
        SELECT loc FROM AnimalVisitedLocation loc JOIN loc.animal a
        WHERE a.id = ?1
        ORDER BY loc.visitedAt ASC
        """)
    List<AnimalVisitedLocation> getSortedAnimalPoints(Long animalId);
}
