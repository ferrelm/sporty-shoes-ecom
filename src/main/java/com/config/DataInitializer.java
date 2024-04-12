package com.config;

import com.entity.Login;
import com.repository.LoginRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(LoginRepository loginRepository, PasswordEncoder passwordEncoder) {
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Use the method findUserByName to check if the admin user already exists
        Optional<Login> admin = loginRepository.findUserByName("admin");
        if (!admin.isPresent()) {  // Check if the admin user does not exist
            // Assuming the Login entity requires a username, password, and roles at minimum
            Login newAdmin = new Login();
            newAdmin.setUsername("admin");
            newAdmin.setPassword(passwordEncoder.encode("securePassword"));  // Ensure you use a secure, encoded password
            newAdmin.setRole("USER,ADMIN");  // Adjust based on your role setup

            loginRepository.save(newAdmin);
        }
    }
}

