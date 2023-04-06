package com.arsuhinars.animals_chipization.animal.repository;

import com.arsuhinars.animals_chipization.animal.model.AnimalLocation;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface AnimalLocationRepository extends CrudRepository<AnimalLocation, Long> {
    @Query("""
        SELECT loc FROM AnimalLocation loc JOIN loc.animal a
        WHERE
            a.id = ?1 AND
            (CAST(?2 AS TIMESTAMP) IS NULL OR loc.visitedAt >= CAST(?2 AS TIMESTAMP)) AND
            (CAST(?3 AS TIMESTAMP) IS NULL OR loc.visitedAt <= CAST(?3 AS TIMESTAMP))
        """)
    Page<AnimalLocation> search(
        Long animalId,
        @Nullable OffsetDateTime start,
        @Nullable OffsetDateTime end,
        Pageable pageable
    );

    @Query("""
        SELECT loc FROM AnimalLocation loc JOIN loc.animal a
        WHERE a.id = ?1
        ORDER BY loc.visitedAt ASC, loc.id ASC
        LIMIT 1
        """)
    Optional<AnimalLocation> getAnimalFirstPoint(Long animalId);

    @Query("""
        SELECT loc FROM AnimalLocation loc JOIN loc.animal a
        WHERE a.id = ?1
        ORDER BY loc.visitedAt DESC, loc.id DESC
        LIMIT 1
        """)
    Optional<AnimalLocation> getAnimalLastPoint(Long animalId);

    @Query("""
        SELECT loc FROM AnimalLocation loc JOIN loc.animal a
        WHERE a.id = ?1
        ORDER BY loc.visitedAt ASC, loc.id ASC
        """)
    List<AnimalLocation> getSortedAnimalPoints(Long animalId);
}
