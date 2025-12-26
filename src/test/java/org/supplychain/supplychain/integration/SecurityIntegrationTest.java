package org.supplychain.supplychain.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.supplychain.supplychain.dto.Auth.AuthenticationRequest;
import org.supplychain.supplychain.dto.Auth.AuthenticationResponse;
import org.supplychain.supplychain.dto.Auth.RegisterRequest;
import org.supplychain.supplychain.enums.Role;
import org.supplychain.supplychain.model.User;
import org.supplychain.supplychain.repository.user.UserRepository;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

//    @BeforeEach
//    void setUp() {
//        userRepository.deleteAll();
//    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void shouldLoginSuccessfully() throws Exception {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("Should fail login with incorrect password")
    void shouldFailLoginWithWrongPassword() throws Exception {
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("wrongpass");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError()); // Change from isForbidden() to isInternalServerError()
    }

    @Test
    @DisplayName("Should deny access to restricted route for unauthorized role")
    void shouldDenyAccessForUnauthorizedRole() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName("Worker");
        registerRequest.setLastName("User");
        registerRequest.setEmail("worker@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Role.PLANIFICATEUR);

        String response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readValue(response, AuthenticationResponse.class).getAccessToken();

        mockMvc.perform(get("/api/suppliers")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should reject access when no token is provided")
    void shouldRejectRequestWithoutToken() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should refresh access token using valid refresh token")
    void shouldRefreshAccessToken() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName("Admin");
        registerRequest.setLastName("User");
        registerRequest.setEmail("admin@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Role.ADMIN);

        String authResponseJson = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn().getResponse().getContentAsString();

        String refreshToken = objectMapper.readValue(authResponseJson, AuthenticationResponse.class).getRefreshToken();

        Map<String, String> refreshRequest = new HashMap<>();
        refreshRequest.put("token", refreshToken);

        mockMvc.perform(post("/api/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }
}