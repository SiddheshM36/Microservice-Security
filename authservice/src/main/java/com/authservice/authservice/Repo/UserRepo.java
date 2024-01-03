package com.authservice.authservice.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authservice.authservice.Models.UserModel;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserModel, Long>{
    
    Optional<UserModel> findByEmail(String email); 
}
