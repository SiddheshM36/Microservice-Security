package com.authservice.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authservice.authservice.Models.UserModel;
import com.authservice.authservice.Repo.UserRepo;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    public UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

  @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserModel> optionalUser = userRepo.findByEmail(username);
    return optionalUser.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
}

}
