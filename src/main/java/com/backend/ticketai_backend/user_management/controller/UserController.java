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


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

   @GetMapping("/users")
   public ResponseEntity<?> getMethodName(@RequestParam String param) {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
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
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
            Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail()
            )
        );
    }
   

}
