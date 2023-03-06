package com.arsuhinars.animals_chipization.controller;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.schema.account.AccountCreateSchema;
import com.arsuhinars.animals_chipization.schema.account.AccountSchema;
import com.arsuhinars.animals_chipization.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/registration")
@Validated
public class RegisterController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    public AccountSchema registerAccount(
        @RequestBody AccountCreateSchema account
    ) throws AlreadyExistException {
        return accountService.create(account);
    }
}
