package com.backend.ticketai_backend.user_management.controller;

import com.backend.ticketai_backend.employee_management.dto.LoginRequestDto;
import com.backend.ticketai_backend.ticket_management.model.Ticket;
import com.backend.ticketai_backend.ticket_management.service.TicketService;
import com.backend.ticketai_backend.user_management.dto.RegisterRequestDto;
import com.backend.ticketai_backend.user_management.model.User;
import com.backend.ticketai_backend.user_management.service.UserService;
import com.backend.ticketai_backend.util.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginData) {
        User user = userService.login(loginData.getEmail(), loginData.getPassword());
        if (user != null) {
            String token = jwtUtil.generateToken(user.getEmail(), "USER", user.getId());
            return ResponseEntity.ok(Map.of("token", token,"role","USER","id", user.getId(), "name", user.getName(),"email", user.getEmail()));
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());

        User savedUser = userService.register(newUser);
        if (savedUser != null) {
            return ResponseEntity.ok(Map.of("message", "User registered successfully", "user_id", savedUser.getId()));
        } else {
            return ResponseEntity.status(400).body(Map.of("message", "Email already registered"));
        }
    }
 
   @GetMapping("/users")
   public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "No users found"));
        }
       return ResponseEntity.ok(
        users.stream()
            .map(user -> {
                return Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail()
                );
            }).toList()
       );
   }

   @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
        List<Ticket> tickets = ticketService.getTicketsByUserId(id);
        return ResponseEntity.ok(
            Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "tickets",tickets
            )
        );
    }
    // DELETE /api/users/{id}
    @DeleteMapping("/delete_user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        boolean deleted = userService.deleteUserById(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "User deleted successfully", "user_id", id));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
    }

    @DeleteMapping("/delete_account")
    public ResponseEntity<?> deleteAccount(@RequestHeader("Authorization") String token) {
        String userId = jwtUtil.getClaimsFromToken(token.substring(7)).get("id", String.class);
        boolean deleted = userService.deleteUserById(userId);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
    }


}
