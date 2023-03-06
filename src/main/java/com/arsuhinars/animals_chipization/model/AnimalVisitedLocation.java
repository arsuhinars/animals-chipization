package com.arsuhinars.animals_chipization.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "animals_visited_locations")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class AnimalVisitedLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "visited_location_id", nullable = false)
    private Location visitedLocation;

    @NonNull
    LocalDateTime visitedAt;
}