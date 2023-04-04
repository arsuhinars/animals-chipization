package com.arsuhinars.animals_chipization.schema.account;

import com.arsuhinars.animals_chipization.enums.AccountRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateSchema {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private AccountRole role;

    public AccountCreateSchema(RegistrationSchema schema) {
        this.firstName = schema.getFirstName();
        this.lastName = schema.getLastName();
        this.email = schema.getEmail();
        this.password = schema.getPassword();
        this.role = AccountRole.USER;
    }
}
