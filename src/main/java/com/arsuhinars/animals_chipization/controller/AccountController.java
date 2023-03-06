package com.arsuhinars.animals_chipization.controller;

import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.account.AccountSchema;
import com.arsuhinars.animals_chipization.service.AccountService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/account")
@Validated
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    public AccountSchema getAccountById(@PathVariable @Min(1) Long id) throws NotFoundException {
        var account = accountService.getById(id);
        if (account == null) {
            throw new NotFoundException("Account was not found");
        }

        return account;
    }
}
