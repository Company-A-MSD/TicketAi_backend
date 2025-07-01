package com.backend.ticketai_backend.user_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.backend.ticketai_backend.user_management.model.User;
import com.backend.ticketai_backend.user_management.repository.UserRepo;

public class UserService {

    @Autowired
    private UserRepo userRepo;

    public List<User> getAllUsers() {
        // This method should interact with the UserRepo to fetch all users.
        // For now, returning an empty list as a placeholder.
        return userRepo.findAll();
    }

    
}
