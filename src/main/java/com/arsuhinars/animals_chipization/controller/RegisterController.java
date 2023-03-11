package com.arsuhinars.animals_chipization.controller;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.schema.account.AccountCreateSchema;
import com.arsuhinars.animals_chipization.schema.account.AccountSchema;
import com.arsuhinars.animals_chipization.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/registration")
public class RegisterController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountSchema registerAccount(
        @Valid @RequestBody AccountCreateSchema account
    ) throws AlreadyExistException {
        return accountService.create(account);
    }
}
