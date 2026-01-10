package org.supplychain.supplychain.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supplychain.supplychain.dto.Auth.AuthenticationRequest;
import org.supplychain.supplychain.dto.Auth.AuthenticationResponse;
import org.supplychain.supplychain.dto.Auth.RefreshTokenRequest;
import org.supplychain.supplychain.dto.Auth.RegisterRequest;
import org.supplychain.supplychain.service.jwtAuth.AuthenticationService;

import java.time.Duration;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        AuthenticationResponse authResponse = service.authenticate(request);

        ResponseCookie refreshCookie = ResponseCookie
                .from("refreshToken", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false) // true in production warning asi mustapha hh
                .path("/api/auth/refresh-token")
                .sameSite("Lax") // strict / lax for localhsot
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

      // we shouldn't return refresh token but for testing and educt porpose
        return ResponseEntity.ok(
                AuthenticationResponse.builder()
                        .accessToken(authResponse.getAccessToken())
                        .refreshToken(authResponse.getRefreshToken()) // testing only
                        .role(authResponse.getRole())
                        .build()
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(service.refreshToken(request.getToken()));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        // Create a cookie with the same name "refreshToken" but with an empty value
        // maxAge = 0 means the cookie will be deleted immediately in the browser
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)             // Prevent JavaScript from accessing the cookie
                .secure(false)              // false for localhost, true in production (HTTPS)
                .path("/api/auth/refresh-token") // Must match the path used when creating the cookie
                .sameSite("Lax")            // Same SameSite attribute as the original cookie
                .maxAge(0)                  // Delete the cookie immediately
                .build();

        // Add the deleted cookie to the response header
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        // Return HTTP 204 No Content → logout successful
        return ResponseEntity.noContent().build();
    }

}