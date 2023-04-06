package com.arsuhinars.animals_chipization.account.controller;

import com.arsuhinars.animals_chipization.account.enums.AccountRole;
import com.arsuhinars.animals_chipization.core.exception.AlreadyExistException;
import com.arsuhinars.animals_chipization.core.exception.ForbiddenException;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.account.model.Account;
import com.arsuhinars.animals_chipization.account.schema.AccountCreateSchema;
import com.arsuhinars.animals_chipization.account.schema.AccountSchema;
import com.arsuhinars.animals_chipization.account.schema.AccountUpdateSchema;
import com.arsuhinars.animals_chipization.core.security.AccountDetails;
import com.arsuhinars.animals_chipization.account.service.AccountService;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@Validated
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public AccountSchema getAccountById(
        @PathVariable @Min(1) Long id,
        Authentication authentication
    ) throws NotFoundException {
        if (!hasAccessToAccount(authentication, id)) {
            throw new ForbiddenException();
        }

        var schema = service.getById(id);
        if (schema.isEmpty()) {
            throw new NotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Account.class, id)
            );
        }

        return schema.get();
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountSchema createAccount(
        @Valid @RequestBody AccountCreateSchema schema
    ) throws AlreadyExistException {
        return service.create(schema);
    }

    @PutMapping("/{id}")
    public AccountSchema updateAccountById(
        @PathVariable @Min(1) Long id,
        @Valid @RequestBody AccountUpdateSchema schema,
        Authentication authentication
    ) throws AlreadyExistException, ForbiddenException {
        if (!hasAccessToAccount(authentication, id)) {
            throw new ForbiddenException();
        }

        return service.update(id, schema);
    }

    @DeleteMapping("/{id}")
    public void deleteAccountById(
        @PathVariable @Min(1) Long id,
        Authentication authentication
    ) {
        if (!hasAccessToAccount(authentication, id)) {
            throw new ForbiddenException();
        }

        service.delete(id);
    }

    private boolean hasAccessToAccount(Authentication auth, Long accountId) {
        var details = (AccountDetails)auth.getPrincipal();
        var account = details.getAccount();

        return account.getRole() == AccountRole.ADMIN || account.getId().equals(accountId);
    }
}
