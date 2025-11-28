package com.example.eventregistration.config;

import com.example.eventregistration.model.User;
import com.example.eventregistration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@eventregistration.com");
            admin.setFullName("System Administrator");
            admin.addRole("ROLE_ADMIN");
            admin.addRole("ROLE_USER");
            userRepository.save(admin);
            System.out.println("Default admin user created - Username: admin, Password: admin123");
        }
    }
}
