package com.arsuhinars.animals_chipization.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "accounts", indexes = {
    @Index(columnList = "email", unique = true)
})
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    @ToString.Include
    private String email;

    @NonNull
    private String hashedPassword;

    @OneToMany(mappedBy = "chipper", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Animal> chippedAnimals;
}
