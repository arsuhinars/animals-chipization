package com.arsuhinars.animals_chipization.area.repository;

import com.arsuhinars.animals_chipization.area.model.Area;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AreaRepository extends CrudRepository<Area, Long> {
    boolean existsByName(String name);

    @Query("""
        SELECT a FROM Area a WHERE
            :x >= a.rect.minX AND :x <= a.rect.maxX AND
            :y >= a.rect.minY AND :y <= a.rect.maxY
        """)
    List<Area> findInPointRange(double x, double y);

    @Query("""
        SELECT a FROM Area a WHERE
            (
                (:minX >= a.rect.minX AND :minX <= a.rect.maxX) OR
                (:maxX >= a.rect.minX AND :maxX <= a.rect.maxX) OR
                (a.rect.minX >= :minX AND a.rect.minX <= :maxX) OR
                (a.rect.maxX >= :minX AND a.rect.maxX <= :maxX)
            ) AND
            (
                (:minY >= a.rect.minY AND :minY <= a.rect.maxY) OR
                (:maxY >= a.rect.minY AND :maxY <= a.rect.maxY) OR
                (a.rect.minY >= :minY AND a.rect.minY <= :maxY) OR
                (a.rect.maxY >= :minY AND a.rect.maxY <= :maxY)
            )
        """)
    List<Area> findInRectRange(double minX, double minY, double maxX, double maxY);
}
