package com.backend.ticketai_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/")
    public String checkDatabaseConnection() {
        try {
            // Try to ping the database
            mongoTemplate.getDb().runCommand(new org.bson.Document("ping", 1));
            return "Database connection successful! MongoDB is running.";
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
}