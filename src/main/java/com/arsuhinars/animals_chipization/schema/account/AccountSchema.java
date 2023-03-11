package com.arsuhinars.animals_chipization.schema.account;

import com.arsuhinars.animals_chipization.model.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSchema {
    @NotNull
    @Min(1)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    public AccountSchema(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public static AccountSchema createFromModel(Account account) {
        return new AccountSchema(
            account.getId(),
            account.getFirstName(),
            account.getLastName(),
            account.getEmail()
        );
    }
}
