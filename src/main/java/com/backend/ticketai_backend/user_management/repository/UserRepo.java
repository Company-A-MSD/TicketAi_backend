package com.backend.ticketai_backend.user_management.repository;

import com.backend.ticketai_backend.user_management.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User,String> {
    

}
