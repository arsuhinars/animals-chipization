package com.arsuhinars.animals_chipization.account.service;

import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.DependsOnException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.account.schema.AccountCreateSchema;
import com.arsuhinars.animals_chipization.account.schema.AccountSchema;
import com.arsuhinars.animals_chipization.account.schema.AccountUpdateSchema;
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
