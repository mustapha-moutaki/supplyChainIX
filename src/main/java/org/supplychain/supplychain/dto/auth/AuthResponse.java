package org.supplychain.supplychain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.supplychain.supplychain.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String message;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Boolean success;

    public AuthResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }
}