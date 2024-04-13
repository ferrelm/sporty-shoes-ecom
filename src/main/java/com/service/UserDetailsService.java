package com.service;

import com.entity.Login;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.repository.LoginRepository;
import java.util.Optional;

@Service
public class UserDetailsService {

    private final LoginRepository loginRepository;

    public UserDetailsService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public Optional<Login> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                return loginRepository.findUserByName(username);
            } else if (principal instanceof Login) {
                return Optional.of((Login) principal);
            }
        }
        return Optional.empty();
    }
}

