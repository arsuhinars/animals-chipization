package com.arsuhinars.animals_chipization.repository;

import com.arsuhinars.animals_chipization.model.AnimalType;
import org.springframework.data.repository.CrudRepository;

public interface AnimalTypeRepository extends CrudRepository<AnimalType, Long> {
    boolean existsByType(String type);
}
