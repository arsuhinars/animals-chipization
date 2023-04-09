package com.arsuhinars.animals_chipization.area.model;

import com.arsuhinars.animals_chipization.area.util.Rect;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @ElementCollection
    @Column(nullable = false)
    private List<AreaPoint> points;

    @NonNull
    @Embedded
    @Column(nullable = false)
    private Rect rect;
}
