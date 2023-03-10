package com.arsuhinars.animals_chipization.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "animals_types", indexes = {
    @Index(columnList = "latitude, latitude", unique = true)
})
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode.Include
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
