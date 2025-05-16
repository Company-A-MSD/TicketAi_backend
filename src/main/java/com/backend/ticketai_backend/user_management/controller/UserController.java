package com.backend.ticketai_backend.user_management.controller;

import com.backend.ticketai_backend.user_management.model.User;
import com.backend.ticketai_backend.user_management.repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepo repo;

    @GetMapping("/users") //Admin only
    public List<User> getUsers(){
        return repo.findAll();
    }

    @GetMapping("/users/{_id}")   //Admin only
    public User getUser(@PathVariable ObjectId _id){
        return repo.findById(_id.toHexString()).orElse(new User());
    }

    @DeleteMapping("/delete_account/{_id}")
    public String deleteAccount(@PathVariable ObjectId _id){
        repo.deleteById(_id.toHexString());
        return "Your account has been successfully deleted!";
    }

    @DeleteMapping("/delete_user/{_id}")    //Admin only
    public String deleteUser(@PathVariable ObjectId _id)  {
        String name = repo.findById(_id.toHexString()).get().getName();
        repo.deleteById(_id.toHexString());
        return "Successfully deleted %s!".formatted(name);
    }

}
