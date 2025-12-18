package org.supplychain.supplychain.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.supplychain.supplychain.dto.Auth.AuthenticationRequest;
import org.supplychain.supplychain.dto.Auth.AuthenticationResponse;
import org.supplychain.supplychain.dto.Auth.RefreshTokenRequest;
import org.supplychain.supplychain.dto.Auth.RegisterRequest;
import org.supplychain.supplychain.service.jwtAuth.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j // Enables logging (log.info, log.warn, log.error)
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        // INFO: Track incoming register requests
        log.info("Register request received email={}", request.getEmail());

        try {
            AuthenticationResponse response = service.register(request);

            // INFO: Successful registration
            log.info("User registered successfully email={}", request.getEmail());

            return ResponseEntity.ok(response);

        } catch (IllegalStateException ex) {
            // WARN: Business rule violation (user already exists)
            log.warn("Register failed - account already exists email={}", request.getEmail());
            throw ex; // let global exception handler manage response

        } catch (Exception ex) {
            // ERROR: Unexpected error
            log.error("Register error email={}", request.getEmail(), ex);
            throw ex;
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        // INFO: Login attempt
        log.info("Authentication attempt email={}", request.getEmail());

        try {
            AuthenticationResponse response = service.authenticate(request);

            // INFO: Successful login
            log.info("Authentication success email={}", request.getEmail());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {
            // WARN: Invalid credentials (expected failure)
            log.warn("Authentication failed - invalid credentials email={}", request.getEmail());
            throw ex;

        } catch (Exception ex) {
            // ERROR: Unexpected authentication error
            log.error("Authentication error email={}", request.getEmail(), ex);
            throw ex;
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        // INFO: Refresh token request
        log.info("Refresh token request received");

        try {
            AuthenticationResponse response = service.refreshToken(request.getToken());

            // INFO: Token refreshed successfully
            log.info("Token refreshed successfully");

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            // ERROR: Token refresh failure
            log.error("Refresh token error", ex);
            throw ex;
        }
    }
}
