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
        Employee emp = employeeRepository.findByEmail(email);
        if (emp != null && emp.getPassword().equals(password)) { // NOTE: Use hashed passwords in production
            return Optional.of(emp);
        }
        return Optional.empty();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee updateEmployee(String id, Employee data) {
        Employee emp = employeeRepository.findById(id).orElse(null);
        if (emp == null) {
            return null; // Employee not found
        }

        if (data.getName() != null) emp.setName(data.getName());
        if (data.getEmail() != null) emp.setEmail(data.getEmail());
        if (data.getRole()!= null) emp.setRole(data.getRole());
        if (data.getAvailability() != null) emp.setAvailability(data.getAvailability());
        if (data.getAssigned_categories() != null)emp.setAssigned_categories(data.getAssigned_categories());
        
        return employeeRepository.save(emp);
    }

    public Employee getEmployeebyEmail(String email)
    {
        return employeeRepository.findByEmail(email);
    }

    public Employee TicketAssignmet(String category)
    {
        List<Employee> employees = employeeRepository.findByAssignedCategories(category);
        if (employees.isEmpty()) {
            return null; // No employee available for this category
        }

        
        int numberOfEmployees = employees.size();
        for (int i = 0; i < numberOfEmployees; i++) 
        {
            if (employees.get(i).getAvailability()) 
            {
                int workload = 0;
                try 
                {
                    workload = Integer.parseInt(employees.get(i).getWorkload());
                    if (workload < 5) 
                    {
                        employees.get(i).setWorkload(String.valueOf(workload + 1)); // Increment workload
                        if (employees.get(i).getWorkload().equals("5")) {
                            employees.get(i).setAvailability(false); // Set availability to false if workload reaches 5
                        }
                        employeeRepository.save(employees.get(i)); // Save updated employee
                        return employees.get(i); // Return the first available employee with workload < 5
                    }
        
                }catch (NumberFormatException e) {
                    // Handle the case where workload is not a valid integer
                    System.err.println("Invalid workload format for employee: " + employees.get(i).getEmail());
                }
            }
        }  
        return null; // No employee available with workload < 5                
    } 
}   