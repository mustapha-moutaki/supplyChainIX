package org.supplychain.supplychain.dto.Auth;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.supplychain.supplychain.enums.Role;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "first name is required")
    private String firstName;
    @NotBlank(message = "last name is required")
    private String lastName;
    @Email
    private String email;
    @NotNull
    @Size(min = 5, message = "the password must be at least 5 characters ")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
