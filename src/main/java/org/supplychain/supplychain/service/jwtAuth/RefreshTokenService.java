package org.supplychain.supplychain.service.jwtAuth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.supplychain.supplychain.model.RefreshToken;
import org.supplychain.supplychain.repository.jwtAuth.RefreshTokenRepository;
import org.supplychain.supplychain.repository.user.UserRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${application.jwt.refresh-token-expiration}")
    private long refreshTokenDurationMs;

    public RefreshToken createRefreshToken(String username) {
        // get user
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // get old token if exist for this user
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(new RefreshToken()); // f not exist create one

        // update data
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());


        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

}