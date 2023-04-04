package com.arsuhinars.animals_chipization.security;

import com.arsuhinars.animals_chipization.enums.AccountRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class AccountRoleAuthority implements GrantedAuthority {
    private AccountRole role;

    @Override
    public String getAuthority() {
        return role.toString();
    }

    public static AccountRoleAuthority userAuthority() {
        return new AccountRoleAuthority(AccountRole.USER);
    }

    public static AccountRoleAuthority chipperAuthority() {
        return new AccountRoleAuthority(AccountRole.CHIPPER);
    }

    public static AccountRoleAuthority adminAuthority() {
        return new AccountRoleAuthority(AccountRole.ADMIN);
    }
}
