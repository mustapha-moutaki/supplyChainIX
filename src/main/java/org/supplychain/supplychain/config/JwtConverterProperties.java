package org.supplychain.supplychain.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.auth.converter")
public class JwtConverterProperties {

    private String resourceId; // Your Keycloak Client ID (e.g., springboot-client)
    private String principalAttribute;
}
