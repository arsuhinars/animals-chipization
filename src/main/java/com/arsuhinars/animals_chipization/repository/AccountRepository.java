package com.arsuhinars.animals_chipization.repository;

import com.arsuhinars.animals_chipization.model.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(
        value = """
            SELECT *, set_limit(0.1) FROM accounts WHERE
                (?1 IS NULL OR LOWER(accounts.first_name) % ?1) AND
                (?2 IS NULL OR LOWER(accounts.last_name) % ?2) AND
                (?3 IS NULL OR LOWER(accounts.email) % ?3)
            ORDER BY accounts.id ASC
            LIMIT ?4
            OFFSET ?5
            """,
        nativeQuery = true
    )
    List<Account> search(String firstName, String lastName, String email, int limit, int offset);
}
