package com.arsuhinars.animals_chipization.core.security;

import com.arsuhinars.animals_chipization.account.schema.AccountSchema;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AccountDetails implements UserDetails {
    @Getter
    private final AccountSchema account;
    private final String hashedPassword;
    private final Collection<AccountRoleAuthority> authorities;

    public AccountDetails(AccountSchema account, String hashedPassword) {
        this.account = account;
        this.hashedPassword = hashedPassword;
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
        return hashedPassword;
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
