package com.arsuhinars.animals_chipization.repository;

import com.arsuhinars.animals_chipization.model.Animal;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AnimalRepository extends CrudRepository<Animal, Long> {
    public Optional<Animal> findByChipperId(Long chipperId);

    public boolean existsByChipperId(Long chipperId);
}
