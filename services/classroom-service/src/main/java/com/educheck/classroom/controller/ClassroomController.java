package com.educheck.classroom.controller;


import com.educheck.classroom.dto.ClassroomResponseDto;
import com.educheck.classroom.dto.CreateClassroomDto;
import com.educheck.classroom.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @GetMapping("/health")
    public Map<String, String > health(){
        return Map.of(
                "service", "classroom-service",
                "status","up"
        );
    }

    @GetMapping("/{id}/ownership")
    public ResponseEntity<Map<String, Boolean>> checkOwnership(
            @PathVariable Long id,
            HttpServletRequest request) {

        String email = (String) request.getAttribute("authenticatedEmail");
        String role = (String) request.getAttribute("authenticatedRole");

        boolean isOwner = classroomService.isOwner(id, email, role);

        return ResponseEntity.ok(Map.of("owner", isOwner));
    }

    @GetMapping
    public ResponseEntity<List<ClassroomResponseDto>> getAllClassrooms() {
        return ResponseEntity.ok(
                classroomService.getAllClassrooms()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponseDto> getClassroomById(@PathVariable Long id){
        return ResponseEntity.ok(
                classroomService.getClassroomById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomResponseDto> updateClassroom(
            @PathVariable Long id,
            @Valid @RequestBody CreateClassroomDto dto,
            HttpServletRequest request) {

        String email = (String) request.getAttribute("authenticatedEmail");
        String role = (String) request.getAttribute("authenticatedRole");

        return ResponseEntity.ok(
                classroomService.updateClassroom(id, dto, email, role)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }



}
