package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.DependsOnException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.account.AccountCreateSchema;
import com.arsuhinars.animals_chipization.schema.account.AccountSchema;
import com.arsuhinars.animals_chipization.schema.account.AccountUpdateSchema;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    AccountSchema create(AccountCreateSchema schema) throws AlreadyExistException;

    Optional<AccountSchema> getById(Long id);

    Optional<AccountSchema> getByEmail(String email);

    List<AccountSchema> search(
        @Nullable String firstName,
        @Nullable String lastName,
        @Nullable String email,
        int from, int count
    );

    AccountSchema update(Long id, AccountUpdateSchema schema) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, DependsOnException;
}
