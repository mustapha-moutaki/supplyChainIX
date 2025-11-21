package org.supplychain.supplychain.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.supplychain.supplychain.model.User;
import org.supplychain.supplychain.repository.user.UserRepository;


import java.util.Collections;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Set<GrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority(user.getRole().name())
        );

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // Using email as username
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}