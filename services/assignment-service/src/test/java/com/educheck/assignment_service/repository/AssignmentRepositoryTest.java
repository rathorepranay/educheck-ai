package com.educheck.assignment_service.repository;

import com.educheck.assignment_service.entity.Assignment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@TestPropertySource(properties = {
        "spring.datasource.password=educheck321"
})
class AssignmentRepositoryTest {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Test
    void saveAssignment_shouldPersistAndRetrieveAssignment() {
        Assignment assignment = new Assignment();
        assignment.setClassroomId(1L);
        assignment.setTitle("Integration Test Assignment");
        assignment.setDescription("Testing database persistence");
        assignment.setDueDate(LocalDate.now().plusDays(7));

        Assignment saved =
                assignmentRepository.saveAndFlush(assignment);

        assertThat(saved.getId()).isNotNull();

        Assignment retrieved = assignmentRepository
                .findById(saved.getId())
                .orElseThrow();

        assertThat(retrieved.getClassroomId()).isEqualTo(1L);
        assertThat(retrieved.getTitle())
                .isEqualTo("Integration Test Assignment");
        assertThat(retrieved.getDescription())
                .isEqualTo("Testing database persistence");
        assertThat(retrieved.getCreatedAt()).isNotNull();
    }
}