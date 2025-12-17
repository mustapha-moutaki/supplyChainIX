package org.supplychain.supplychain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private String secret;
    private long accessTokenExpiration;  // 15 min
    private long refreshTokenExpiration; // 7 days
}
