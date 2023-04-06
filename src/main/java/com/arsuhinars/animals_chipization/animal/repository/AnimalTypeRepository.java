package com.arsuhinars.animals_chipization.animal.repository;

import com.arsuhinars.animals_chipization.animal.model.AnimalType;
import org.springframework.data.repository.CrudRepository;

public interface AnimalTypeRepository extends CrudRepository<AnimalType, Long> {
    boolean existsByType(String type);
}
