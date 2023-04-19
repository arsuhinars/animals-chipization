package com.arsuhinars.animals_chipization.area.model;

import com.arsuhinars.animals_chipization.core.util.GeoPosition;
import com.arsuhinars.animals_chipization.core.util.Rect;
import com.arsuhinars.animals_chipization.location.model.Location;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "area", indexes = {
    @Index(columnList = "name", unique = true),
    @Index(columnList = "minX"), @Index(columnList = "maxX"),
    @Index(columnList = "minY"), @Index(columnList = "maxY")
})
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @ElementCollection
    @Column(nullable = false)
    private List<GeoPosition> points;

    @NonNull
    @Embedded
    @Column(nullable = false)
    private Rect rect;

    @ManyToMany(mappedBy = "areas")
    private Set<Location> locations;
}
