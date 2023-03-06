package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.BoundException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.account.AccountCreateSchema;
import com.arsuhinars.animals_chipization.schema.account.AccountSchema;
import com.arsuhinars.animals_chipization.schema.account.AccountUpdateSchema;

public interface AccountService {
    AccountSchema create(AccountCreateSchema account) throws AlreadyExistException;

    AccountSchema getById(Long id);

    AccountSchema getByEmail(String email);

    AccountSchema update(Long id, AccountUpdateSchema account) throws NotFoundException, AlreadyExistException;

    void delete(Long id) throws NotFoundException, BoundException;
}
