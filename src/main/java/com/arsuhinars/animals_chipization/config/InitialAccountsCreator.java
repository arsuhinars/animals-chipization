package com.arsuhinars.animals_chipization.config;

import com.arsuhinars.animals_chipization.enums.AccountRole;
import com.arsuhinars.animals_chipization.schema.account.AccountCreateSchema;
import com.arsuhinars.animals_chipization.service.AccountService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitialAccountsCreator implements ApplicationRunner {
    private static final List<AccountCreateSchema> INITIAL_ACCOUNTS = List.of(
        new AccountCreateSchema(
            "adminFirstName",
            "adminLastName",
            "admin@simbirsoft.com",
            "qwerty123",
            AccountRole.ADMIN
        ),
        new AccountCreateSchema(
            "chipperFirstName",
            "chipperLastName",
            "chipper@simbirsoft.com",
            "qwerty123",
            AccountRole.CHIPPER
        ),
        new AccountCreateSchema(
            "userFirstName",
            "userLastName",
            "user@simbirsoft.com",
            "qwerty123",
            AccountRole.USER
        )
    );

    private final AccountService accountService;

    public InitialAccountsCreator(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (var schema : INITIAL_ACCOUNTS) {
            if (accountService.getByEmail(schema.getEmail()).isEmpty()) {
                accountService.create(schema);
            }
        }
    }
}
