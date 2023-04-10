package com.arsuhinars.animals_chipization.account.controller;

import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.account.schema.AccountCreateSchema;
import com.arsuhinars.animals_chipization.account.schema.AccountSchema;
import com.arsuhinars.animals_chipization.account.schema.RegistrationSchema;
import com.arsuhinars.animals_chipization.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/registration")
public class RegistrationController {
    private final AccountService service;

    public RegistrationController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountSchema registerAccount(
        @Valid @RequestBody RegistrationSchema schema
    ) throws AlreadyExistException {
        return new AccountSchema(
            service.create(new AccountCreateSchema(schema))
        );
    }
}
