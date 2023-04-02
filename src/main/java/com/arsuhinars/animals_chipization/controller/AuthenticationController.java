package com.arsuhinars.animals_chipization.controller;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.schema.account.AccountCreateSchema;
import com.arsuhinars.animals_chipization.schema.account.AccountSchema;
import com.arsuhinars.animals_chipization.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/registration")
public class AuthenticationController {
    private final AccountService service;

    public AuthenticationController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountSchema registerAccount(
        @Valid @RequestBody AccountCreateSchema account
    ) throws AlreadyExistException {
        return service.create(account);
    }
}
