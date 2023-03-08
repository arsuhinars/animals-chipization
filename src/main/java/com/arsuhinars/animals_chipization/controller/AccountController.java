package com.arsuhinars.animals_chipization.controller;

import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.account.AccountSchema;
import com.arsuhinars.animals_chipization.service.AccountService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
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

    @GetMapping("/search")
    public List<AccountSchema> searchAccounts(
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(defaultValue = "0") @Min(0) Integer from,
        @RequestParam(defaultValue = "10") @Min(1) Integer size
    ) {
        return accountService.search(firstName, lastName, email, from, size);
    }
}
