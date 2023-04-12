package com.arsuhinars.animals_chipization.account.service;

import com.arsuhinars.animals_chipization.account.model.Account;
import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.DependsOnException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.account.schema.AccountCreateSchema;
import com.arsuhinars.animals_chipization.account.schema.AccountUpdateSchema;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account create(AccountCreateSchema schema) throws AlreadyExistException;

    Optional<Account> getById(Long id);

    Optional<Account> getByEmail(String email);

    List<Account> search(
        @Nullable String firstName,
        @Nullable String lastName,
        @Nullable String email,
        int from, int count
    );

    Account update(Long id, AccountUpdateSchema schema) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, DependsOnException;
}
