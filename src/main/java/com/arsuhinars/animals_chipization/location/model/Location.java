package com.arsuhinars.animals_chipization.location.model;

import com.arsuhinars.animals_chipization.animal.model.Animal;
import com.arsuhinars.animals_chipization.animal.model.AnimalLocation;
import com.arsuhinars.animals_chipization.area.model.Area;
import com.arsuhinars.animals_chipization.core.util.GeoPosition;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "locations", indexes = {
    @Index(columnList = "latitude, longitude", unique = true),
    @Index(columnList = "latitude"),
    @Index(columnList = "longitude")
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
    @AttributeOverrides({
        @AttributeOverride(name = "latitude",  column = @Column(name = "latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
    })
    private GeoPosition position;

    @NonNull
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "locations_areas",
        joinColumns = @JoinColumn(name = "location_id"),
        inverseJoinColumns = @JoinColumn(name = "area_id")
    )
    private Set<Area> areas;

    @OneToMany(mappedBy = "chippingLocation")
    @ToString.Exclude
    private Set<Animal> chippedAnimals;

    @OneToMany(mappedBy = "visitedLocation")
    @ToString.Exclude
    private Set<AnimalLocation> visitedAnimals;
}
