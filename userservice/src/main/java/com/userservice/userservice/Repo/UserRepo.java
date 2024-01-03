package com.userservice.userservice.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.userservice.userservice.Models.UserModel;
import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<UserModel, Long>{
    
    Optional<UserModel> findByEmail(String email);
}
