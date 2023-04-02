package com.arsuhinars.animals_chipization.repository;

import com.arsuhinars.animals_chipization.model.Account;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(
        value = """
            SELECT * FROM accounts WHERE
                (?1 IS NULL OR accounts.first_name ILIKE CONCAT('%', ?1, '%')) AND
                (?2 IS NULL OR accounts.last_name ILIKE CONCAT('%', ?2, '%')) AND
                (?3 IS NULL OR accounts.email ILIKE CONCAT('%', ?3, '%'))
            ORDER BY accounts.id ASC
            LIMIT ?4
            OFFSET ?5
        """,
        nativeQuery = true
    )
    List<Account> search(
        @Nullable String firstName,
        @Nullable String lastName,
        @Nullable String email,
        int limit,
        int offset
    );
}
