package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.DependsOnException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.model.Account;
import com.arsuhinars.animals_chipization.model.Animal;
import com.arsuhinars.animals_chipization.repository.AccountRepository;
import com.arsuhinars.animals_chipization.schema.account.AccountCreateSchema;
import com.arsuhinars.animals_chipization.schema.account.AccountSchema;
import com.arsuhinars.animals_chipization.schema.account.AccountUpdateSchema;
import com.arsuhinars.animals_chipization.util.ErrorDetailsFormatter;
import jakarta.annotation.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AccountSchema create(AccountCreateSchema schema) throws AlreadyExistException {
        if (repository.existsByEmail(schema.getEmail())) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(Account.class, "email", schema.getEmail())
            );
        }

        var account = new Account(
            schema.getFirstName(),
            schema.getLastName(),
            schema.getEmail(),
            passwordEncoder.encode(schema.getPassword())
        );

        return new AccountSchema(repository.save(account));
    }

    @Override
    public Optional<AccountSchema> getById(Long id) {
        return repository.findById(id).map(AccountSchema::new);
    }

    @Override
    public Optional<AccountSchema> getByEmail(String email) {
        return repository.findByEmail(email).map(AccountSchema::new);
    }

    @Override
    public List<AccountSchema> search(
        @Nullable String firstName,
        @Nullable String lastName,
        @Nullable String email,
        int from, int count
    ) {
        return
            repository.search(firstName, lastName, email, count, from)
            .stream()
            .map(AccountSchema::new)
            .toList();
    }

    @Override
    public AccountSchema update(Long id, AccountUpdateSchema schema) throws NotFoundException, AlreadyExistException {
        var account = repository.findById(id).orElse(null);
        if (account == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Account.class, id)
            );
        }

        if (!account.getEmail().equals(schema.getEmail()) &&
            repository.existsByEmail(schema.getEmail())
        ) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(Account.class, "email", schema.getEmail())
            );
        }

        account.setFirstName(schema.getFirstName());
        account.setLastName(schema.getLastName());
        account.setEmail(schema.getEmail());
        account.setHashedPassword(
            passwordEncoder.encode(schema.getPassword())
        );

        return new AccountSchema(repository.save(account));
    }

    @Override
    public void delete(Long id) throws NotFoundException, DependsOnException {
        var account = repository.findById(id).orElse(null);
        if (account == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Account.class, id)
            );
        }

        if (!account.getChippedAnimals().isEmpty()) {
            throw new DependsOnException(
                ErrorDetailsFormatter.formatDependsOnError(account, Animal.class)
            );
        }

        repository.deleteById(id);
    }
}
