package com.scms.service;

import com.scms.dto.StudentDTO;
import com.scms.entity.Student;
import com.scms.exception.ResourceNotFoundException;
import com.scms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Service layer - business logic + conversion between Entity and DTO
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Convert Entity -> DTO
    private StudentDTO toDTO(Student student) {
        return new StudentDTO(student.getId(), student.getName(), student.getEmail(), student.getAge());
    }

    // Convert DTO -> Entity
    private Student toEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setAge(dto.getAge());
        return student;
    }

    // Create a new student
    public StudentDTO createStudent(StudentDTO dto) {
        Student saved = studentRepository.save(toEntity(dto));
        return toDTO(saved);
    }

    // Get all students
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Get single student by id, throws exception if not found
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return toDTO(student);
    }

    // Update existing student, throws exception if not found
    public StudentDTO updateStudent(Long id, StudentDTO dto) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setAge(dto.getAge());
        return toDTO(studentRepository.save(existing));
    }

    // Delete student, throws exception if not found
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}