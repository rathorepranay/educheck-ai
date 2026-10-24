
package com.educheck.assignment_service.service;

import com.educheck.assignment_service.client.ClassroomClient;
import com.educheck.assignment_service.dto.AssignmentResponseDto;
import com.educheck.assignment_service.dto.CreateAssignmentDto;
import com.educheck.assignment_service.entity.Assignment;
import com.educheck.assignment_service.exception.ResourceNotFoundException;
import com.educheck.assignment_service.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ClassroomClient classroomClient;

    public AssignmentResponseDto createAssignment(
            CreateAssignmentDto dto, String email, String role) {

        classroomClient.validateClassroom(dto.classroomId());

        if ("TEACHER".equals(role)
                && !classroomClient.isClassroomOwner(dto.classroomId())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    "You can only create assignments in your own classrooms"
            );
        }

        Assignment assignment = new Assignment();
        assignment.setClassroomId(dto.classroomId());
        assignment.setTitle(dto.title());
        assignment.setDescription(dto.description());
        assignment.setDueDate(dto.dueDate());
        assignment.setCreatedBy(email);

        return toDto(assignmentRepository.save(assignment));
    }
    @Transactional(readOnly = true)
    public List<AssignmentResponseDto> getAllAssignments() {
        return assignmentRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public AssignmentResponseDto getAssignmentById(Long id) {
        return toDto(findAssignment(id));
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponseDto> getAssignmentsByClassroom(
            Long classroomId) {

        return assignmentRepository.findByClassroomId(classroomId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public AssignmentResponseDto updateAssignment(
            Long id, CreateAssignmentDto dto, String email, String role) {

        Assignment assignment = findAssignment(id);

        // 1. Verify assignment ownership
        if ("ADMIN".equals(role)) {
            // Admins can update any assignment.
        } else if (!"TEACHER".equals(role)
                || assignment.getCreatedBy() == null
                || email == null
                || !assignment.getCreatedBy().equalsIgnoreCase(email)) {

            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    "You are not allowed to modify this assignment"
            );
        }

        // 2. Verify the destination classroom exists
        classroomClient.validateClassroom(dto.classroomId());

        // 3. Teachers can only use classrooms they own
        if ("TEACHER".equals(role)
                && !classroomClient.isClassroomOwner(dto.classroomId())) {

            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    "You can only move assignments to your own classrooms"
            );
        }

        // 4. Apply the updates
        assignment.setClassroomId(dto.classroomId());
        assignment.setTitle(dto.title());
        assignment.setDescription(dto.description());
        assignment.setDueDate(dto.dueDate());

        return toDto(assignmentRepository.save(assignment));
    } public void deleteAssignment(Long id) {
        assignmentRepository.delete(findAssignment(id));
    }

    private Assignment findAssignment(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Assignment not found with id: " + id));
    }

    private AssignmentResponseDto toDto(Assignment assignment) {
        return new AssignmentResponseDto(
                assignment.getId(),
                assignment.getClassroomId(),
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getDueDate(),
                assignment.getCreatedAt()
        );
    }
}
