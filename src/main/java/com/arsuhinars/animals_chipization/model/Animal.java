package com.arsuhinars.animals_chipization.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "animals", indexes = {
    @Index(columnList = "chippingDateTime"),
    @Index(columnList = "lifeStatus"),
    @Index(columnList = "gender")
})
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "types",
        joinColumns = @JoinColumn(name = "animal_id"),
        inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private Set<AnimalType> types;

    @NonNull
    private Float weight;

    @NonNull
    private Float length;

    @NonNull
    private Float height;

    @NonNull
    @Enumerated(EnumType.ORDINAL)
    private AnimalGender gender;

    @NonNull
    private LifeStatus lifeStatus;

    @NonNull
    private OffsetDateTime chippingDateTime;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "chipper_id", nullable = false)
    private Account chipper;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "chipping_location_id", nullable = false)
    private Location chippingLocation;

    @NonNull
    @OneToMany(
        mappedBy = "animal",
        fetch = FetchType.LAZY,
        cascade = { CascadeType.PERSIST, CascadeType.MERGE },
        orphanRemoval = true
    )
    private Set<AnimalVisitedLocation> visitedLocations;

    private OffsetDateTime deathDateTime;
}
