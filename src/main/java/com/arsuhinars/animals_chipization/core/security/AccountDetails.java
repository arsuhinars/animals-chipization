package com.arsuhinars.animals_chipization.core.security;

import com.arsuhinars.animals_chipization.account.model.Account;
import com.arsuhinars.animals_chipization.account.schema.AccountSchema;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AccountDetails implements UserDetails {
    @Getter
    private final Account account;
    private final Collection<AccountRoleAuthority> authorities;

    public AccountDetails(Account account) {
        this.account = account;
        this.authorities = Collections.singleton(
            new AccountRoleAuthority(account.getRole())
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getHashedPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
