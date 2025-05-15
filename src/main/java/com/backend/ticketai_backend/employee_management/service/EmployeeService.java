package com.backend.ticketai_backend.employee_management.service;

import com.backend.ticketai_backend.employee_management.model.Employee;
import com.backend.ticketai_backend.employee_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Optional<Employee> login(String email, String password) {
        return employeeRepository.findByEmail(email)
                .filter(emp -> emp.getPassword().equals(password)); // NOTE: Use hashed passwords in production
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    public Employee updateAvailability(String id, Boolean status) {
        Employee emp = employeeRepository.findById(id).orElseThrow();
        emp.setAvailability(status);
        return employeeRepository.save(emp);
    }

    public Employee updateEmployee(String id, Employee data) {
        Employee emp = employeeRepository.findById(id).orElseThrow();
        emp.setName(data.getName());
        emp.setEmail(data.getEmail());
        emp.setRole(data.getRole());
        emp.setAvailability(data.getAvailability());
        emp.setAssigned_categories(data.getAssigned_categories());
        return employeeRepository.save(emp);
        
    }
}