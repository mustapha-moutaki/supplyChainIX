package org.supplychain.supplychain.service.user;

import org.supplychain.supplychain.dto.auth.RegisterRequest;
import org.supplychain.supplychain.model.User;

import java.util.Optional;

public interface UserService {

    User registerUser(RegisterRequest registerRequest);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}