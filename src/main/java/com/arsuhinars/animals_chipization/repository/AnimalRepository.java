package com.arsuhinars.animals_chipization.repository;

import com.arsuhinars.animals_chipization.model.Animal;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AnimalRepository extends CrudRepository<Animal, Long> {
    Optional<Animal> findByChipperId(Long chipperId);

    boolean existsByChipperId(Long chipperId);

    boolean existsByTypesId(Long typeId);
}
