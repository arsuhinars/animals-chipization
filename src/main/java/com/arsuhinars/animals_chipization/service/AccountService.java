package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.BoundException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.account.AccountCreateSchema;
import com.arsuhinars.animals_chipization.schema.account.AccountSchema;
import com.arsuhinars.animals_chipization.schema.account.AccountUpdateSchema;

import java.util.List;

public interface AccountService {
    AccountSchema create(AccountCreateSchema account) throws AlreadyExistException;

    AccountSchema getById(Long id);

    AccountSchema getByEmail(String email);

    List<AccountSchema> search(String firstName, String lastName, String email, int from, int count);

    AccountSchema update(Long id, AccountUpdateSchema account) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, BoundException;
}
