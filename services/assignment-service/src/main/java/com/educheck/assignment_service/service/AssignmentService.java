
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
            CreateAssignmentDto dto) {
        classroomClient.validateClassroom(dto.classroomId());

        Assignment assignment = new Assignment();
        assignment.setClassroomId(dto.classroomId());
        assignment.setTitle(dto.title());
        assignment.setDescription(dto.description());
        assignment.setDueDate(dto.dueDate());

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
            Long id, CreateAssignmentDto dto) {
        classroomClient.validateClassroom(dto.classroomId());

        Assignment assignment = findAssignment(id);

        assignment.setClassroomId(dto.classroomId());
        assignment.setTitle(dto.title());
        assignment.setDescription(dto.description());
        assignment.setDueDate(dto.dueDate());

        return toDto(assignmentRepository.save(assignment));
    }

    public void deleteAssignment(Long id) {
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
