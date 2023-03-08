package com.arsuhinars.animals_chipization.repository;

import com.arsuhinars.animals_chipization.model.Account;
import org.springframework.data.domain.Pageable;
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
                (?1 IS NULL OR accounts.first_name % ?1) AND
                (?2 IS NULL OR accounts.last_name % ?2) AND
                (?3 IS NULL OR accounts.email % ?3)
            -- #pageable
            """,
        countQuery = "SELECT count(*) FROM accounts",
        nativeQuery = true
    )
    List<Account> search(String firstName, String lastName, String email, Pageable pageable);
}
