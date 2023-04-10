package com.arsuhinars.animals_chipization.account.service;

import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.DependsOnException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.account.model.Account;
import com.arsuhinars.animals_chipization.animal.model.Animal;
import com.arsuhinars.animals_chipization.account.repository.AccountRepository;
import com.arsuhinars.animals_chipization.account.schema.AccountCreateSchema;
import com.arsuhinars.animals_chipization.account.schema.AccountSchema;
import com.arsuhinars.animals_chipization.account.schema.AccountUpdateSchema;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
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
    public Account create(AccountCreateSchema schema) throws AlreadyExistException {
        if (repository.existsByEmail(schema.getEmail())) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(Account.class, "email", schema.getEmail())
            );
        }

        var account = new Account(
            schema.getFirstName(),
            schema.getLastName(),
            schema.getEmail(),
            passwordEncoder.encode(schema.getPassword()),
            schema.getRole()
        );

        return repository.save(account);
    }

    @Override
    public Optional<Account> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Account> getByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public List<Account> search(
        @Nullable String firstName,
        @Nullable String lastName,
        @Nullable String email,
        int from, int count
    ) {
        return repository.search(firstName, lastName, email, count, from);
    }

    @Override
    public Account update(Long id, AccountUpdateSchema schema) throws NotFoundException, AlreadyExistException {
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
        account.setRole(schema.getRole());

        return repository.save(account);
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
