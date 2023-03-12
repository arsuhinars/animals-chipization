package com.arsuhinars.animals_chipization.service;

import com.arsuhinars.animals_chipization.model.Account;
import com.arsuhinars.animals_chipization.repository.AccountRepository;
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
        var dbAccount = accountRepository.findByEmail(email).orElse(null);
        if (dbAccount == null) {
            throw new UsernameNotFoundException(
                ErrorDetailsFormatter.formatNotFoundError(Account.class, "email", email)
            );
        }

        return new AccountDetails(dbAccount);
    }
}
