package com.educheck.classroom.service;

import com.educheck.classroom.dto.ClassroomResponseDto;
import com.educheck.classroom.dto.CreateClassroomDto;
import com.educheck.classroom.entity.Classroom;
import com.educheck.classroom.exception.ResourceNotFoundException;
import com.educheck.classroom.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    public ClassroomResponseDto createClassroom(CreateClassroomDto dto) {
        Classroom classroom = new Classroom();
        classroom.setName(dto.name());
        classroom.setDescription(dto.description());

        return toDto(classroomRepository.save(classroom));
    }

    @Transactional(readOnly = true)
    public List<ClassroomResponseDto> getAllClassrooms() {
        return classroomRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClassroomResponseDto getClassroomById(Long id) {
        return toDto(findClassroom(id));
    }

    public ClassroomResponseDto updateClassroom(
            Long id, CreateClassroomDto dto) {
        Classroom classroom = findClassroom(id);
        classroom.setName(dto.name());
        classroom.setDescription(dto.description());

        return toDto(classroomRepository.save(classroom));
    }

    public void deleteClassroom(Long id) {
        classroomRepository.delete(findClassroom(id));
    }

    private Classroom findClassroom(Long id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Classroom not found with id: " + id));
    }

    private ClassroomResponseDto toDto(Classroom classroom) {
        return new ClassroomResponseDto(
                classroom.getId(),
                classroom.getName(),
                classroom.getDescription(),
                classroom.getCreatedAt()
        );
    }
}
