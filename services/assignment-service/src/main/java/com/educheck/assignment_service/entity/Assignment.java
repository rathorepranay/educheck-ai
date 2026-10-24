package com.educheck.assignment_service.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "assignments")
@NoArgsConstructor
@Getter
@Setter
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "classroom_id", nullable = false)
    private  Long classroomId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 500)
    private  String description;




    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "created_by", length = 255)
    private String createdBy;


    private Instant createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
    }

}
