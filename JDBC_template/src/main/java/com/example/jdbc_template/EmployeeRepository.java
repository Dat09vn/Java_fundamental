package com.example.jdbc_template;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Employee> mapper = (rs, rowNum) -> {

        Employee employee = new Employee();

        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setEmail(rs.getString("email"));

        return employee;
    };

    public Long save(Employee employee) {
        String sql = "INSERT INTO employees (name, email) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "ID" });
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key != null ? key.longValue() : null;
    }

    // Using RowMapper to map the result set to Employee objects
    public Employee findById(Long id) {
        String sql = "SELECT id, name, email FROM employees WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, mapper, id);
    }

    public String findNameById(Long id) {
        String sql = "SELECT name FROM employees WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    //using BeanPropertyRowMapper to map the result set to Employee objects
    public List<Employee> findAll() {
        String sql = "SELECT id, name, email FROM employees ORDER BY id";
        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Employee.class));
    }

    public int updateEmail(Long id, String newEmail) {
        String sql = "UPDATE employees SET email = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newEmail, id);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
