package com.arsuhinars.animals_chipization.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "animals_types")
@Data
@NoArgsConstructor
public class AnimalType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NonNull
    private String type;

    @ManyToMany(mappedBy = "types")
    private Set<Animal> animals;
}
