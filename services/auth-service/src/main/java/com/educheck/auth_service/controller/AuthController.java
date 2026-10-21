package com.educheck.auth_service.controller;

import com.educheck.auth_service.dto.LoginRequestDto;
import com.educheck.auth_service.dto.LoginResponseDto;
import com.educheck.auth_service.dto.RegistrationRequestDto;
import com.educheck.auth_service.dto.RegistrationResponseDto;
import com.educheck.auth_service.service.AuthService;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Getter
@Setter
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> register(
            @Valid @RequestBody RegistrationRequestDto requestDto) {

        RegistrationResponseDto response = authService.register(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto){

        LoginResponseDto responseDto = authService.login(requestDto);

        return ResponseEntity.ok(responseDto);

    }
}