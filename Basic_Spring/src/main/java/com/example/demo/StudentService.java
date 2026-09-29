package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final List<Student> students = new ArrayList<>();

    public StudentService() {
        students.add(new Student(1L, "Alice", 20));
        students.add(new Student(2L, "Bob", 22));
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public Student getStudentById(Long id) {
        return students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Student createStudent(Student student) {
        students.add(student);
        return student;
    }
}
