package com.backend.ticketai_backend.employee_management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.ticketai_backend.employee_management.dto.LoginRequestDto;
import com.backend.ticketai_backend.employee_management.model.Employee;
import com.backend.ticketai_backend.employee_management.service.EmployeeService;
import com.backend.ticketai_backend.util.JwtUtil;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtUtil jwtUtil;

    

    // 1. LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginData) {
        return employeeService.login(loginData.getEmail(), loginData.getPassword())
                .map(emp -> {
                    String token = jwtUtil.generateToken(emp.getEmail(), emp.getRole(), emp.get_id());
                    return ResponseEntity.ok(Map.of("token", token,"role",emp.getRole()));
                })
                .orElse(ResponseEntity.ok(Map.of("message", "Invalid credentials")));
    }


    // 3. GET ALL EMPLOYEES
    @GetMapping
    public ResponseEntity<?> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();

        if(employees.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No employees found"));
        }

        List<Map<String, Object>> result = employees.stream().map(emp -> Map.of(
                "employee_id", (Object) emp.get_id(),
                "name", (Object) emp.getName(),
                "email", (Object) emp.getEmail(),
                "role", (Object) emp.getRole(),
                "availability", (Object) emp.getAvailability(),
                "assigned_categories", (Object) emp.getAssigned_categories(),
                "workload", (Object) emp.getWorkload()
        )).toList();
        return ResponseEntity.ok(result);
    }

    // 4. GET EMPLOYEE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable String id) {

        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
           return ResponseEntity.ok(Map.of(
                "employee_id", employee.get_id(),
                "name", employee.getName(),
                "email", employee.getEmail(),
                "role", employee.getRole(),
                "availability", employee.getAvailability(),
                "assigned_categories", employee.getAssigned_categories(),
                "workload", employee.getWorkload()
            ));
        }
        else {
            return ResponseEntity.ok(Map.of("message", "Employee not found"));
        }
      
    }

    // 5. UPDATE EMPLOYEE DETAILS
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody Employee updatedData) {
        Employee updated = employeeService.updateEmployee(id, updatedData);
        return ResponseEntity.ok(Map.of("message", "Employee updated successfully", "employee_id", updated.get_id()));
    }
}