package org.supplychain.supplychain.service.jwtAuth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.supplychain.supplychain.dto.Auth.AuthenticationRequest;
import org.supplychain.supplychain.dto.Auth.AuthenticationResponse;
import org.supplychain.supplychain.dto.Auth.RegisterRequest;
import org.supplychain.supplychain.model.RefreshToken;
import org.supplychain.supplychain.model.User;

import org.supplychain.supplychain.repository.jwtAuth.RefreshTokenRepository;
import org.supplychain.supplychain.repository.user.UserRepository;
import org.supplychain.supplychain.security.CustomUserDetails;
import org.supplychain.supplychain.security.JwtService;
import org.supplychain.supplychain.service.jwtAuth.RefreshTokenService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository; // to get the token

    public AuthenticationResponse register(RegisterRequest request) {
        // create new user
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user); //save in db

        // Create tokens
        var jwtToken = jwtService.generateToken(new CustomUserDetails(user));
        var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // check the data spring security check the data here
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // getting the user
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(new CustomUserDetails(user));

        // here we can remove the old token and create new ones
        var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthenticationResponse refreshToken(String requestRefreshToken) {
        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(new CustomUserDetails(user));
                    return AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(requestRefreshToken) // retunr the same refreshTken or Rotation
                            .build();
                }).orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}