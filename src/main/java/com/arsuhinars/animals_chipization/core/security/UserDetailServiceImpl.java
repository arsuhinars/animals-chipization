package com.arsuhinars.animals_chipization.core.security;

import com.arsuhinars.animals_chipization.account.model.Account;
import com.arsuhinars.animals_chipization.account.repository.AccountRepository;
import com.arsuhinars.animals_chipization.account.schema.AccountSchema;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var account = accountRepository.findByEmail(email).orElse(null);
        if (account == null) {
            throw new UsernameNotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Account.class, "email", email)
            );
        }

        return new AccountDetails(
            new AccountSchema(account),
            account.getHashedPassword()
        );
    }
}
