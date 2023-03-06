package com.arsuhinars.animals_chipization.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "animals_types")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NonNull
    private Double latitude;

    @NonNull
    private Double longitude;

    @OneToMany(mappedBy = "chippingLocation")
    private Set<Animal> chippedAnimals;

    @OneToMany(mappedBy = "visitedLocation")
    private Set<AnimalVisitedLocation> visitedAnimals;
}
