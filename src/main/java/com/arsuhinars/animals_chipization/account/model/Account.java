package com.arsuhinars.animals_chipization.account.model;

import com.arsuhinars.animals_chipization.account.enums.AccountRole;
import com.arsuhinars.animals_chipization.animal.model.Animal;
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
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @Column(nullable = false)
    private String lastName;

    @NonNull
    @Column(nullable = false, unique = true)
    @ToString.Include
    private String email;

    @NonNull
    @Column(nullable = false)
    private String hashedPassword;

    @NonNull
    @Column(nullable = false)
    @Enumerated
    private AccountRole role;

    @OneToMany(mappedBy = "chipper", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Animal> chippedAnimals;
}
