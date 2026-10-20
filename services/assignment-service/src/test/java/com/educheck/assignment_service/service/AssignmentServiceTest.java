
package com.educheck.assignment_service.service;

import com.educheck.assignment_service.exception.ResourceNotFoundException;

import static org.mockito.Mockito.doThrow;

import com.educheck.assignment_service.client.ClassroomClient;
import com.educheck.assignment_service.dto.AssignmentResponseDto;
import com.educheck.assignment_service.dto.CreateAssignmentDto;
import com.educheck.assignment_service.entity.Assignment;
import com.educheck.assignment_service.repository.AssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private ClassroomClient classroomClient;

    @InjectMocks
    private AssignmentService assignmentService;

    private CreateAssignmentDto createDto;

    @BeforeEach
    void setUp() {
        createDto = new CreateAssignmentDto(
                1L,
                "Java Assignment",
                "Complete Java exercises",
                LocalDate.now().plusDays(5)
        );
    }

    @Test
    void createAssignment_shouldReturnSavedAssignment() {
        Assignment savedAssignment = new Assignment();
        savedAssignment.setId(10L);
        savedAssignment.setClassroomId(1L);
        savedAssignment.setTitle("Java Assignment");
        savedAssignment.setDescription("Complete Java exercises");
        savedAssignment.setDueDate(createDto.dueDate());
        savedAssignment.setCreatedAt(Instant.now());

        when(assignmentRepository.save(any(Assignment.class)))
                .thenReturn(savedAssignment);

        AssignmentResponseDto result =
                assignmentService.createAssignment(createDto);

        assertNotNull(result);
        assertEquals(10L, result.id());
        assertEquals(1L, result.classroomId());
        assertEquals("Java Assignment", result.title());
        assertEquals("Complete Java exercises", result.description());

        verify(classroomClient).validateClassroom(1L);
        verify(assignmentRepository).save(any(Assignment.class));
    }

    @Test
    void getAssignmentById_whenMissing_shouldThrowException() {
        when(assignmentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                com.educheck.assignment_service.exception.ResourceNotFoundException.class,
                () -> assignmentService.getAssignmentById(999L)
        );

        verify(assignmentRepository).findById(999L);
    }

    @Test
    void createAssignment_whenClassroomDoesNotExist_shouldNotSave() {
        doThrow(new ResourceNotFoundException(
                "Classroom not found with id: 1"))
                .when(classroomClient)
                .validateClassroom(1L);

        assertThrows(
                ResourceNotFoundException.class,
                () -> assignmentService.createAssignment(createDto)
        );

        verify(classroomClient).validateClassroom(1L);
        verify(assignmentRepository, never()).save(any(Assignment.class));
    }

}
