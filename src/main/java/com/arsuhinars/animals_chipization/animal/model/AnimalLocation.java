package com.arsuhinars.animals_chipization.animal.model;

import com.arsuhinars.animals_chipization.location.model.Location;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "animals_visited_locations", indexes = {
    @Index(columnList = "visitedAt")
})
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class AnimalLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "visited_location_id", nullable = false)
    private Location visitedLocation;

    @NonNull
    @Column(nullable = false)
    private OffsetDateTime visitedAt;
}
