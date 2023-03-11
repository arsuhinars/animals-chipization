package com.arsuhinars.animals_chipization.controller;

import com.arsuhinars.animals_chipization.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.exception.ForbiddenException;
import com.arsuhinars.animals_chipization.exception.NotFoundException;
import com.arsuhinars.animals_chipization.schema.account.AccountSchema;
import com.arsuhinars.animals_chipization.schema.account.AccountUpdateSchema;
import com.arsuhinars.animals_chipization.security.AccountDetails;
import com.arsuhinars.animals_chipization.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/accounts")
@Validated
public class AccountController {
    @Autowired
    private AccountService service;

    @GetMapping("/{id}")
    public AccountSchema getAccountById(@PathVariable @Min(1) Long id) throws NotFoundException {
        var account = service.getById(id);
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
        return service.search(firstName, lastName, email, from, size);
    }

    @PutMapping("/{id}")
    public AccountSchema updateAccountById(
        @PathVariable @Min(1) Long id,
        @Valid @RequestBody AccountUpdateSchema account,
        Authentication authentication
    ) throws AlreadyExistException, ForbiddenException {
        var details = (AccountDetails)authentication.getPrincipal();

        if (!Objects.equals(details.getAccount().getId(), id)) {
            throw new ForbiddenException("You can only update your own account");
        }

        return service.update(id, account);
    }

    @DeleteMapping("/{id}")
    public void deleteAccountById(
        @PathVariable @Min(1) Long id,
        Authentication authentication
    ) {
        var details = (AccountDetails)authentication.getPrincipal();

        if (!Objects.equals(details.getAccount().getId(), id)) {
            throw new ForbiddenException("You can only delete your own account");
        }

        service.delete(id);
    }
}
