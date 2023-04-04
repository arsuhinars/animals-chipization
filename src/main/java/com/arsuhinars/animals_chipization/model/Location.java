package com.arsuhinars.animals_chipization.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "locations", indexes = {
    @Index(columnList = "latitude, latitude", unique = true)
})
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @Column(nullable = false)
    private Double latitude;

    @NonNull
    @Column(nullable = false)
    private Double longitude;

    @OneToMany(mappedBy = "chippingLocation")
    @ToString.Exclude
    private Set<Animal> chippedAnimals;

    @OneToMany(mappedBy = "visitedLocation")
    @ToString.Exclude
    private Set<AnimalLocation> visitedAnimals;
}
