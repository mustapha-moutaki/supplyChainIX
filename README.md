# 🔐 Spring Boot Security - Complete Guide

<div align="center">

![Spring Security](https://img.shields.io/badge/Spring%20Security-6.0+-green?style=for-the-badge&logo=spring)
![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

**A comprehensive guide to securing your Spring Boot applications**

[Core Concepts](#-core-concepts) • [Authentication](#-authentication-methods) • [Authorization](#-authorization) • [Code Examples](#-complete-code-examples)

</div>

---

## 📑 Table of Contents

1. [Introduction](#-introduction)
2. [Core Concepts](#-core-concepts)
3. [Authentication Methods](#-authentication-methods)
4. [Authorization](#-authorization)
5. [Password Encoding](#-password-encoding--hashing-algorithms)
6. [Session Management](#-session-management)
7. [JWT Implementation](#-jwt-json-web-token)
8. [OAuth2 & Social Login](#-oauth2--social-login)
9. [CORS Configuration](#-cors-configuration)
10. [Security Attacks & Prevention](#-security-attacks--prevention)
11. [Complete Code Examples](#-complete-code-examples)
12. [Best Practices](#-best-practices)
13. [Troubleshooting](#-troubleshooting)

---

## 🎯 Introduction

Spring Security is a powerful and highly customizable authentication and access-control framework for Java applications. It is the de-facto standard for securing Spring-based applications.

### Key Features

| Feature | Description |
|---------|-------------|
| **Authentication** | Verify user identity (who are you?) |
| **Authorization** | Control access to resources (what can you do?) |
| **Protection** | Guard against common attacks (CSRF, XSS, etc.) |
| **Integration** | Support for LDAP, OAuth2, SAML, JWT |

### Dependencies

```xml
<!-- Maven -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- For JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
```

```groovy
// Gradle
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'io.jsonwebtoken:jjwt-api:0.12.3'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.3'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.3'
```

---

## 🏗 Core Concepts

### Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        HTTP Request                              │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                   Security Filter Chain                          │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐           │
│  │  CORS    │→│  CSRF    │→│  Auth    │→│ Exception│→ ...      │
│  │  Filter  │ │  Filter  │ │  Filter  │ │  Filter  │           │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘           │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                   Authentication Manager                         │
│                              │                                   │
│                              ▼                                   │
│                   Authentication Provider                        │
│                              │                                   │
│                              ▼                                   │
│                   UserDetailsService                             │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Security Context                            │
│              (Stores Authentication Object)                      │
└─────────────────────────────────────────────────────────────────┘
```

### Core Components

| Component | Description | Responsibility |
|-----------|-------------|----------------|
| `SecurityFilterChain` | Chain of security filters | Process each HTTP request through multiple filters |
| `AuthenticationManager` | Main authentication interface | Coordinate authentication process |
| `AuthenticationProvider` | Performs actual authentication | Validate credentials against data source |
| `UserDetailsService` | Load user-specific data | Retrieve user from database/memory |
| `UserDetails` | User information container | Hold username, password, authorities |
| `SecurityContext` | Security data holder | Store authentication for current thread |
| `SecurityContextHolder` | Access to SecurityContext | Provide static methods to access context |
| `GrantedAuthority` | Permission representation | Represent a single authority/role |
| `PasswordEncoder` | Password hashing interface | Encode and verify passwords |

### Security Filter Chain (Default Filters)

| Order | Filter | Purpose |
|-------|--------|---------|
| 1 | `DisableEncodeUrlFilter` | Disable URL encoding of session ID |
| 2 | `WebAsyncManagerIntegrationFilter` | Async request security context |
| 3 | `SecurityContextHolderFilter` | Populate SecurityContext |
| 4 | `HeaderWriterFilter` | Add security headers |
| 5 | `CorsFilter` | Handle CORS preflight |
| 6 | `CsrfFilter` | CSRF protection |
| 7 | `LogoutFilter` | Process logout |
| 8 | `UsernamePasswordAuthenticationFilter` | Form login processing |
| 9 | `BasicAuthenticationFilter` | HTTP Basic authentication |
| 10 | `RequestCacheAwareFilter` | Cache requests |
| 11 | `SecurityContextHolderAwareRequestFilter` | Servlet API integration |
| 12 | `AnonymousAuthenticationFilter` | Anonymous user handling |
| 13 | `ExceptionTranslationFilter` | Handle security exceptions |
| 14 | `AuthorizationFilter` | Authorization decisions |

---

## 🔐 Authentication Methods

### Comparison Table

| Method | Security Level | Use Case | Stateless |
|--------|---------------|----------|-----------|
| Basic Auth | ⭐⭐ | Simple APIs, Testing | No |
| Form Login | ⭐⭐⭐ | Web Applications | No |
| Session-Based | ⭐⭐⭐ | Traditional Web Apps | No |
| JWT | ⭐⭐⭐⭐ | REST APIs, Microservices | Yes |
| OAuth2 | ⭐⭐⭐⭐⭐ | Third-party Integration | Yes |
| API Key | ⭐⭐⭐ | Service-to-Service | Yes |

### 1. Basic Authentication

```java
@Configuration
@EnableWebSecurity
public class BasicAuthConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }
}
```

**How it works:**
```
Authorization: Basic base64(username:password)
Authorization: Basic YWRtaW46cGFzc3dvcmQ=
```

### 2. Form Login Authentication

```java
@Configuration
@EnableWebSecurity
public class FormLoginConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .usernameParameter("email")
                .passwordParameter("pass")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/login?logout=true")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            );
        
        return http.build();
    }
}
```

### 3. Session-Based vs Token-Based

```
┌─────────────────────────────────────────────────────────────────┐
│                    SESSION-BASED AUTH                            │
├─────────────────────────────────────────────────────────────────┤
│  Client                              Server                      │
│    │                                   │                         │
│    │──── Login (credentials) ─────────▶│                         │
│    │                                   │ Create Session          │
│    │◀─── Set-Cookie: JSESSIONID ──────│ Store in Memory/DB      │
│    │                                   │                         │
│    │──── Request + Cookie ────────────▶│                         │
│    │                                   │ Validate Session        │
│    │◀─── Response ────────────────────│                         │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    TOKEN-BASED AUTH (JWT)                        │
├─────────────────────────────────────────────────────────────────┤
│  Client                              Server                      │
│    │                                   │                         │
│    │──── Login (credentials) ─────────▶│                         │
│    │                                   │ Generate JWT            │
│    │◀─── JWT Token ───────────────────│ (No server storage)     │
│    │                                   │                         │
│    │──── Request + Bearer Token ──────▶│                         │
│    │                                   │ Validate Token          │
│    │◀─── Response ────────────────────│                         │
└─────────────────────────────────────────────────────────────────┘
```

| Feature | Session-Based | Token-Based (JWT) |
|---------|---------------|-------------------|
| Storage | Server-side | Client-side |
| Scalability | ❌ Hard (sticky sessions) | ✅ Easy (stateless) |
| Mobile Support | ❌ Limited | ✅ Excellent |
| Microservices | ❌ Complex | ✅ Perfect fit |
| Revocation | ✅ Easy | ❌ Hard (need blacklist) |
| Size | Small (session ID) | Larger (token payload) |

---

## 🛡 Authorization

### Roles vs Authorities

| Aspect | Roles | Authorities |
|--------|-------|-------------|
| Prefix | `ROLE_` prefix required | No prefix needed |
| Granularity | Coarse-grained | Fine-grained |
| Example | `ROLE_ADMIN`, `ROLE_USER` | `READ`, `WRITE`, `DELETE` |
| Check Method | `hasRole("ADMIN")` | `hasAuthority("READ")` |

### URL-Based Authorization

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            // Public endpoints
            .requestMatchers("/", "/home", "/about").permitAll()
            .requestMatchers("/api/public/**").permitAll()
            
            // Static resources
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
            
            // Role-based access
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
            
            // Authority-based access
            .requestMatchers("/api/users/**").hasAuthority("USER_READ")
            .requestMatchers(HttpMethod.POST, "/api/users/**").hasAuthority("USER_WRITE")
            .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority("USER_DELETE")
            
            // Multiple conditions
            .requestMatchers("/api/reports/**").hasAnyAuthority("REPORT_VIEW", "REPORT_GENERATE")
            
            // IP-based (for internal services)
            .requestMatchers("/actuator/**").hasIpAddress("192.168.1.0/24")
            
            // All other requests need authentication
            .anyRequest().authenticated()
        );
    
    return http.build();
}
```

### Method-Level Security

```java
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class MethodSecurityConfig {
}
```

```java
@Service
public class UserService {

    // ===== @PreAuthorize - Check BEFORE method execution =====
    
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {
        // Only ADMIN can delete users
    }
    
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public User createUser(UserDTO dto) {
        // Only users with USER_WRITE authority
    }
    
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public User getUser(String username) {
        // ADMIN or the user themselves
    }
    
    @PreAuthorize("#user.username == authentication.name")
    public void updateProfile(User user) {
        // Only update own profile
    }
    
    // ===== @PostAuthorize - Check AFTER method execution =====
    
    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    public User findById(Long id) {
        // Return only if it's their own data or they're admin
        return userRepository.findById(id).orElseThrow();
    }
    
    // ===== @PreFilter - Filter input collection =====
    
    @PreFilter("filterObject.owner == authentication.name")
    public void processDocuments(List<Document> documents) {
        // Only process documents owned by current user
    }
    
    // ===== @PostFilter - Filter output collection =====
    
    @PostFilter("filterObject.visible == true or filterObject.owner == authentication.name")
    public List<Document> getAllDocuments() {
        // Return only visible docs or owned by current user
        return documentRepository.findAll();
    }
    
    // ===== @Secured - Simple role check =====
    
    @Secured("ROLE_ADMIN")
    public void adminOnlyMethod() { }
    
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void adminOrManagerMethod() { }
    
    // ===== @RolesAllowed (JSR-250) =====
    
    @RolesAllowed("ADMIN")
    public void jsr250AdminMethod() { }
}
```

### Custom Authorization Logic

```java
@Component("authz")
public class CustomAuthorizationLogic {

    public boolean canAccessUser(Authentication auth, Long userId) {
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        
        UserDetails user = (UserDetails) auth.getPrincipal();
        return userRepository.findById(userId)
            .map(u -> u.getUsername().equals(user.getUsername()))
            .orElse(false);
    }
    
    public boolean isOwner(Authentication auth, Long resourceId) {
        String username = auth.getName();
        return resourceRepository.findById(resourceId)
            .map(r -> r.getOwner().equals(username))
            .orElse(false);
    }
}

// Usage in service
@Service
public class UserService {

    @PreAuthorize("@authz.canAccessUser(authentication, #userId)")
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }
    
    @PreAuthorize("@authz.isOwner(authentication, #documentId)")
    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }
}
```

---

## 🔑 Password Encoding & Hashing Algorithms

### Algorithm Comparison

| Algorithm | Security | Speed | Salt | Use Case |
|-----------|----------|-------|------|----------|
| MD5 | ❌ Broken | ⚡ Very Fast | ❌ No | **Never use** |
| SHA-1 | ❌ Weak | ⚡ Fast | ❌ No | **Never use** |
| SHA-256 | ⚠️ OK | ⚡ Fast | ❌ No | Checksums only |
| BCrypt | ✅ Strong | 🐢 Slow | ✅ Yes | **Recommended** |
| SCrypt | ✅ Strong | 🐢 Slow | ✅ Yes | Memory-hard |
| Argon2 | ✅ Strongest | 🐢 Slow | ✅ Yes | **Best choice** |

### Why BCrypt?

```
Same password with different salts:
"password123" → $2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqZlHg0zPEJ7Q1rJQq8dZ3y7gK/Oy
"password123" → $2a$10$ZLhnHxdpHETcxmtEStgpI./Ri1mksgJ9iDP36FmfMdYyVg9g0b2/q
                      ↑
                Work Factor (cost)
```

### Password Encoder Configuration

```java
@Configuration
public class PasswordConfig {

    // Recommended: BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Work factor: 10-14
    }
    
    // Alternative: Argon2 (strongest)
    @Bean
    public PasswordEncoder argon2Encoder() {
        return new Argon2PasswordEncoder(16, 32, 1, 65536, 3);
    }
    
    // For migration: Delegating encoder
    @Bean
    public PasswordEncoder delegatingPasswordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("argon2", new Argon2PasswordEncoder());
        
        DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder("bcrypt", encoders);
        encoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
        return encoder;
    }
}
```

### Password Encoding Example

```java
@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User register(RegisterDTO dto) {
        // Check if user exists
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken");
        }
        
        // Create new user with encoded password
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Encode!
        user.setRole("USER");
        
        return userRepository.save(user);
    }
    
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
```

---

## 📦 Session Management

### Configuration Options

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .sessionManagement(session -> session
            // Session creation policy
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            
            // Session fixation protection
            .sessionFixation(fixation -> fixation.newSession())
            
            // Maximum sessions per user
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false) // false = kick previous session
            .expiredUrl("/session-expired")
            
            // Session timeout
            .invalidSessionUrl("/invalid-session")
        );
    
    return http.build();
}
```

### Session Creation Policies

| Policy | Description | Use Case |
|--------|-------------|----------|
| `ALWAYS` | Always create a session | Traditional web apps |
| `IF_REQUIRED` | Create only when needed (default) | Most web apps |
| `NEVER` | Never create, but use if exists | Hybrid apps |
| `STATELESS` | Never create or use sessions | REST APIs with JWT |

### Session Fixation Protection

```java
.sessionFixation(fixation -> fixation
    .newSession()        // Create new session, copy attributes
    // OR
    .migrateSession()    // Migrate to new session ID (default)
    // OR
    .changeSessionId()   // Change session ID only (Servlet 3.1+)
    // OR
    .none()              // No protection (NOT recommended!)
)
```

### Remember-Me Configuration

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .rememberMe(remember -> remember
            .key("uniqueAndSecretKey")
            .tokenValiditySeconds(86400 * 30) // 30 days
            .rememberMeParameter("remember-me")
            .userDetailsService(userDetailsService)
            // Persistent tokens (more secure)
            .tokenRepository(persistentTokenRepository())
        );
    
    return http.build();
}

@Bean
public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepo = new JdbcTokenRepositoryImpl();
    tokenRepo.setDataSource(dataSource);
    // Create table on first run
    // tokenRepo.setCreateTableOnStartup(true);
    return tokenRepo;
}
```

---

## 🎫 JWT (JSON Web Token)

### JWT Structure

```
┌─────────────────────────────────────────────────────────────────┐
│                         JWT TOKEN                                │
├──────────────────┬──────────────────┬───────────────────────────┤
│      HEADER      │     PAYLOAD      │        SIGNATURE          │
│    (Algorithm)   │    (Claims)      │    (Verification)         │
├──────────────────┼──────────────────┼───────────────────────────┤
│ {                │ {                │                           │
│   "alg": "HS256",│   "sub": "user1",│  HMACSHA256(             │
│   "typ": "JWT"   │   "iat": 171234, │    base64(header) + "." + │
│ }                │   "exp": 171834, │    base64(payload),       │
│                  │   "roles": [...] │    secret                 │
│                  │ }                │  )                        │
├──────────────────┴──────────────────┴───────────────────────────┤
│ eyJhbGciOiJIUzI1.eyJzdWIiOiJ1c2VyMSI.SflKxwRJSMeKKF2QT4fwpM     │
│     (Header)          (Payload)            (Signature)          │
└─────────────────────────────────────────────────────────────────┘
```

### Access Token vs Refresh Token

| Feature | Access Token | Refresh Token |
|---------|--------------|---------------|
| Purpose | Access resources | Get new access token |
| Lifetime | Short (15 min - 1 hour) | Long (7 days - 30 days) |
| Storage | Memory/LocalStorage | HttpOnly Cookie |
| Sent with | Every API request | Only to /refresh endpoint |
| Contains | User info, permissions | Just user identifier |

### Refresh Token Strategies

| Strategy | Description | Security Level |
|----------|-------------|----------------|
| **Static** | Same token until expiry | ⭐⭐ Lower |
| **Rotating** | New refresh token on each use | ⭐⭐⭐⭐ Higher |
| **Sliding** | Expiry extends on each use | ⭐⭐⭐ Medium |

### Complete JWT Implementation

#### 1. JWT Service

```java
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;
    
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()));
        
        return buildToken(claims, userDetails, accessTokenExpiration);
    }
    
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration);
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
            .claims(claims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), Jwts.SIG.HS256)
            .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
```

#### 2. JWT Authentication Filter

```java
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        // Skip filter for public endpoints
        if (request.getServletPath().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract Authorization header
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);

        // If username exists and no authentication in context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

#### 3. Security Configuration for JWT

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setContentType("application/json");
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + e.getMessage() + "\"}");
                        })
                        .accessDeniedHandler((req, res, e) -> {
                            res.setContentType("application/json");
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"Access denied\"}");
                        })
                );

        return http.build();
    }
}

  ```

# In french
![1.png](documentationOverView/1.png)
![2.png](documentationOverView/2.png)
![3.png](documentationOverView/3.png)
![4.png](documentationOverView/4.png)
![5.png](documentationOverView/5.png)
![6.png](documentationOverView/6.png)
![7.png](documentationOverView/7.png)
![8.png](documentationOverView/8.png)
![9.png](documentationOverView/9.png)
![10.png](documentationOverView/10.png)
![11.png](documentationOverView/11.png)
![12.png](documentationOverView/12.png)
![13.png](documentationOverView/13.png)
![14.png](documentationOverView/14.png)

