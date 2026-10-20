package com.educheck.assignment_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/assignments")
public class AssignmentHealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "up",
                "service", "assignment-service"
        );
    }
}