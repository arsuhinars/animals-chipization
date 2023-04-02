package com.arsuhinars.animals_chipization.security;

import com.arsuhinars.animals_chipization.model.Account;
import com.arsuhinars.animals_chipization.repository.AccountRepository;
import com.arsuhinars.animals_chipization.schema.account.AccountSchema;
import com.arsuhinars.animals_chipization.security.AccountDetails;
import com.arsuhinars.animals_chipization.util.ErrorDetailsFormatter;
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
