
package com.educheck.assignment_service.controller;

import com.educheck.assignment_service.dto.AssignmentResponseDto;
import com.educheck.assignment_service.dto.CreateAssignmentDto;
import com.educheck.assignment_service.service.AssignmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<AssignmentResponseDto> createAssignment(
            @Valid @RequestBody CreateAssignmentDto dto,
            HttpServletRequest request) {

        String email = (String) request.getAttribute("authenticatedEmail");
        String role = (String) request.getAttribute("authenticatedRole");

        return ResponseEntity.status(HttpStatus.CREATED).body(
                assignmentService.createAssignment(dto, email, role)
        );
    }

    @GetMapping
    public ResponseEntity<List<AssignmentResponseDto>> getAllAssignments() {
        return ResponseEntity.ok(
                assignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponseDto> getAssignmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                assignmentService.getAssignmentById(id));
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<AssignmentResponseDto>>
    getAssignmentsByClassroom(
            @PathVariable Long classroomId) {

        return ResponseEntity.ok(
                assignmentService.getAssignmentsByClassroom(classroomId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentResponseDto> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody CreateAssignmentDto dto,
            HttpServletRequest request) {

        String email = (String) request.getAttribute("authenticatedEmail");
        String role = (String) request.getAttribute("authenticatedRole");

        return ResponseEntity.ok(
                assignmentService.updateAssignment(id, dto, email, role)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(
            @PathVariable Long id) {

        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
