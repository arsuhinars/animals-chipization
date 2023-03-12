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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AccountSchema create(AccountCreateSchema account) throws AlreadyExistException {
        if (repository.existsByEmail(account.getEmail())) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(Account.class, "email", account.getEmail())
            );
        }

        var dbAccount = new Account(
            account.getFirstName(),
            account.getLastName(),
            account.getEmail(),
            passwordEncoder.encode(account.getPassword())
        );

        return AccountSchema.createFromModel(
            repository.save(dbAccount)
        );
    }

    @Override
    public AccountSchema getById(Long id) {
        return repository.findById(id).map(AccountSchema::createFromModel).orElse(null);
    }

    @Override
    public AccountSchema getByEmail(String email) {
        return repository.findByEmail(email).map(AccountSchema::createFromModel).orElse(null);
    }

    @Override
    public List<AccountSchema> search(String firstName, String lastName, String email, int from, int count) {
        return repository.search(firstName, lastName, email, count, from)
            .stream()
            .map(AccountSchema::createFromModel)
            .toList();
    }

    @Override
    public AccountSchema update(Long id, AccountUpdateSchema account) throws NotFoundException, AlreadyExistException {
        var dbAccount = repository.findById(id).orElse(null);
        if (dbAccount == null) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Account.class, id)
            );
        }

        if (!dbAccount.getEmail().equals(account.getEmail()) &&
            repository.existsByEmail(account.getEmail())) {
            throw new AlreadyExistException(
                ErrorDetailsFormatter.formatAlreadyExistsError(Account.class, "email", account.getEmail())
            );
        }

        dbAccount.setFirstName(account.getFirstName());
        dbAccount.setLastName(account.getLastName());
        dbAccount.setEmail(account.getEmail());
        dbAccount.setHashedPassword(
            passwordEncoder.encode(account.getPassword())
        );

        return AccountSchema.createFromModel(repository.save(dbAccount));
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
