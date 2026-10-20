
package com.educheck.assignment_service.controller;

import com.educheck.assignment_service.dto.AssignmentResponseDto;
import com.educheck.assignment_service.dto.CreateAssignmentDto;
import com.educheck.assignment_service.service.AssignmentService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssignmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssignmentService assignmentService;

    @Test
    void getAllAssignments_shouldReturnOk() throws Exception {

        when(assignmentService.getAllAssignments())
                .thenReturn(List.of());

        mockMvc.perform(get("/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAssignmentById_shouldReturnAssignment() throws Exception {

        AssignmentResponseDto response = new AssignmentResponseDto(
                1L,
                10L,
                "Java Assignment",
                "Complete the exercises",
                LocalDate.now().plusDays(7),
                Instant.now()
        );

        when(assignmentService.getAssignmentById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/assignments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.classroomId").value(10))
                .andExpect(jsonPath("$.title").value("Java Assignment"));
    }

    @Test
    void createAssignment_shouldReturnCreated() throws Exception {

        LocalDate dueDate = LocalDate.now().plusDays(7);

        AssignmentResponseDto response = new AssignmentResponseDto(
                1L,
                10L,
                "Java Assignment",
                "Complete the exercises",
                dueDate,
                Instant.now()
        );

        when(assignmentService.createAssignment(
                any(CreateAssignmentDto.class)))
                .thenReturn(response);

        String requestJson = """
        {
          "classroomId": 10,
          "title": "Java Assignment",
          "description": "Complete the exercises",
          "dueDate": "%s"
        }
        """.formatted(dueDate);

        mockMvc.perform(post("/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Java Assignment"));
    }

    @Test
    void deleteAssignment_shouldReturnNoContent() throws Exception {

        doNothing().when(assignmentService).deleteAssignment(1L);

        mockMvc.perform(delete("/assignments/1"))
                .andExpect(status().isNoContent());
    }
}
