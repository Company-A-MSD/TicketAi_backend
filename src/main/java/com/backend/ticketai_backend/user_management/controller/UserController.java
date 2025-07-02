package com.backend.ticketai_backend.user_management.controller;

import com.backend.ticketai_backend.user_management.model.User;
import com.backend.ticketai_backend.user_management.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }

    @PostMapping("/register")
    

    

   @GetMapping("/users")
   public ResponseEntity<?> getAllUsers(@RequestParam String param) {
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
        return ResponseEntity.ok(
            Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail()
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


}
