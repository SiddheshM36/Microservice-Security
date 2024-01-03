package com.userservice.userservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Optional;
import com.userservice.userservice.Models.UserModel;
import com.userservice.userservice.Repo.UserRepo;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/add-user")
    public ResponseEntity<UserModel> addNewUser(@RequestBody UserModel userModel){
        userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        UserModel newUser = userRepo.save(userModel);
        return (ResponseEntity<UserModel>)status(200).body(newUser);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<UserModel>> allUsers(){
        List<UserModel> allUsersList = userRepo.findAll();
        return (ResponseEntity<List<UserModel>>)status(200).body(allUsersList);
    }

     @GetMapping("/user/{email}")
    public ResponseEntity<UserModel> getUser(@PathVariable String email){
        Optional<UserModel> presentUser = userRepo.findByEmail(email);
        if(presentUser.isPresent()){
            return (ResponseEntity<UserModel>)status(200).body(presentUser.get());
        }
        else{
            System.out.println("User not exixts");
            return (ResponseEntity<UserModel>)status(400);
        }
        
    }
}
