package com.arsuhinars.animals_chipization.schema.account;

import com.arsuhinars.animals_chipization.model.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class AccountSchema {
    @NonNull
    @NotNull
    @Min(1)
    private Long id;

    @NonNull
    @NotBlank
    private String firstName;

    @NonNull
    @NotBlank
    private String lastName;

    @NonNull
    @NotBlank
    @Email
    private String email;

    public static AccountSchema createFromModel(Account account) {
        return new AccountSchema(
                account.getId(),
                account.getFirstName(),
                account.getLastName(),
                account.getEmail()
        );
    }
}
