package com.educheck.classroom.controller;


import com.educheck.classroom.dto.ClassroomResponseDto;
import com.educheck.classroom.dto.CreateClassroomDto;
import com.educheck.classroom.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<ClassroomResponseDto> createClassroom(@Valid @RequestBody CreateClassroomDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                classroomService.createClassroom(dto)
        );
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
    public ResponseEntity<ClassroomResponseDto> updateClassroom(@PathVariable Long id, @Valid @RequestBody CreateClassroomDto dto){
        return ResponseEntity.ok(
                classroomService.updateClassroom(id,dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }



}
