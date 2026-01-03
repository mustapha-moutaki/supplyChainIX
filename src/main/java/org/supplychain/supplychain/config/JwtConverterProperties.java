package org.supplychain.supplychain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * PURPOSE:
 * This class is a "Properties Mapper." Its job is to read specific values from your
 * 'application.yml' file and turn them into a Java Object.
 *
 * WHY DO WE NEED IT?
 * 1. To avoid hardcoding: Instead of writing "springboot-client" directly in your code,
 *    you write it in the YAML file. This makes it easier to change settings without recompiling.
 * 2. Type Safety: It ensures that the settings you define in your configuration files
 *    are correctly mapped to Java fields.
 *
 * HOW/WHERE IS IT USED?
 * This class is injected into 'JwtConverter.java'. The converter uses:
 * - 'resourceId' to know which "Client Roles" to look for inside the Keycloak Token.
 * - 'principalAttribute' to know which field (like 'preferred_username') should be
 *    treated as the user's unique name.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "application.auth.converter")
public class JwtConverterProperties {

    /**
     * Maps to: application.auth.converter.resource-id
     * Used to identify the Keycloak Client (e.g., supplychain-app).
     */
    private String resourceId;

    /**
     * Maps to: application.auth.converter.principal-attribute
     * Used to decide which JWT claim is the username (e.g., preferred_username).
     */
    private String principalAttribute;
}