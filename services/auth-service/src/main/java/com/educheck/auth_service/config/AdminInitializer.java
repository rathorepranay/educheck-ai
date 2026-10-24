package com.educheck.auth_service.config;

import com.educheck.auth_service.entity.Role;
import com.educheck.auth_service.entity.User;
import com.educheck.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initializeAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email:}") String adminEmail,
            @Value("${app.admin.password:}") String adminPassword,
            @Value("${app.admin.enabled:false}") boolean enabled) {

        return args -> {
            if (!enabled) {
                return;
            }

            if (adminEmail.isBlank() || adminPassword.isBlank()) {
                throw new IllegalStateException(
                        "Admin initialization requires an email and password"
                );
            }

            String email = adminEmail.trim()
                    .toLowerCase(Locale.ROOT);

            if (userRepository.existsByEmail(email)) {
                System.out.println(
                        "Configured admin account already exists."
                );
                return;
            }

            User admin = new User();
            admin.setFullName("System Administrator");
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("Initial admin account created.");
        };
    }
}