package com.educheck.auth_service.service;

import com.educheck.auth_service.dto.LoginRequestDto;
import com.educheck.auth_service.dto.LoginResponseDto;
import com.educheck.auth_service.dto.RegistrationRequestDto;
import com.educheck.auth_service.dto.RegistrationResponseDto;
import com.educheck.auth_service.entity.Role;
import com.educheck.auth_service.entity.User;
import com.educheck.auth_service.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static com.educheck.auth_service.entity.Role.ADMIN;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public RegistrationResponseDto register(
            RegistrationRequestDto requestDto) {

        String email = requestDto.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        // Check whether the email already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "An account with this email already exists"
            );
        }

        // Validate the requested role
        Role role;

        try {
            role = Role.valueOf(
                    requestDto.role()
                            .trim()
                            .toUpperCase(Locale.ROOT)
            );
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new IllegalArgumentException(
                    "Role must be STUDENT or TEACHER"
            );
        }

        // Prevent self-registration as an administrator
        if (role == ADMIN) {
            throw new IllegalArgumentException(
                    "You cannot register as an administrator"
            );
        }

        // Create the user
        User user = new User();
        user.setFullName(requestDto.fullName().trim());
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(requestDto.password())
        );
        user.setRole(role);

        // Save the user
        User savedUser = userRepository.save(user);

        // Return safe response without the password
        return new RegistrationResponseDto(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        );
    }

    public LoginResponseDto login(LoginRequestDto requestDto){
        String email = requestDto.email().trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(email).
                orElseThrow(()-> new IllegalArgumentException("Invalid email or password"));

        if(!passwordEncoder.matches(requestDto.password(), user.getPassword())){
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponseDto(
                token,
                "Bearer",
                3600,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
