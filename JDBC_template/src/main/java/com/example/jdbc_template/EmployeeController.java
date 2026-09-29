package com.example.jdbc_template;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        Long id = employeeRepository.save(employee);
        employee.setId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @GetMapping
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Employee findById(@PathVariable("id") Long id) {
        return employeeRepository.findById(id);
    }

    @GetMapping("/{id}/name")
    public String findNameById(@PathVariable("id") Long id) {
        return employeeRepository.findNameById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmail(
            @PathVariable("id") Long id,
            @RequestBody Employee employee) {
        int updatedRows = employeeRepository.updateEmail(id, employee.getEmail());
        if (updatedRows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(employeeRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        int deletedRows = employeeRepository.deleteById(id);
        if (deletedRows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}