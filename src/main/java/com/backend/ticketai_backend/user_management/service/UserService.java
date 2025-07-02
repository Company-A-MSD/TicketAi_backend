package com.backend.ticketai_backend.user_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.ticketai_backend.user_management.model.User;
import com.backend.ticketai_backend.user_management.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public List<User> getAllUsers() {
        // This method should interact with the UserRepo to fetch all users.
        // For now, returning an empty list as a placeholder.
        return userRepo.findAll();
    }

    public User getUserById(String id) {
        return userRepo.findById(id).orElse(null);
    }
    
    public boolean deleteUserById(String id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }
    
    public User login(String email, String password) {
        User user = userRepo.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; // or throw an exception
    }

    public User register(User newUser) {
        if (userRepo.findByEmail(newUser.getEmail()) != null) {
            return null; // or throw an exception
        }
        return userRepo.save(newUser);
    }

    
}
