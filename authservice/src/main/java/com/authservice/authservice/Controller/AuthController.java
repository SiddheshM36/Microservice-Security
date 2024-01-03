package com.authservice.authservice.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.springframework.http.ResponseEntity.status;

import java.util.ArrayList;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.authservice.authservice.DTO.JWTToken;
import com.authservice.authservice.DTO.LoginDTO;
import com.authservice.authservice.DTO.UserDTO;
import com.authservice.authservice.config.JWTConfig;
import com.authservice.authservice.config.SecurityConfig;

import io.jsonwebtoken.Claims;
import jakarta.ws.rs.HttpMethod;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public JWTConfig jwtConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
         // Get the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println("Current logged-in user: " + username);
            
            ArrayList<UserDTO> getUsers = restTemplate.getForObject("http://localhost:8000/users/all-users", ArrayList.class);
            return ResponseEntity.status(200).body(getUsers);
        } else {
            // Handle unauthenticated user (optional)
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }
    

    @PostMapping("/login")
    private ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        System.out.println("i ma called");
        // first check if user is present in db or not by hitting userservice
        String email = loginDTO.getEmail();
        System.out.println("Email is: " + email);

        UserDTO getUsers = restTemplate.getForObject("http://localhost:8000/users/user/" + email, UserDTO.class);
        if (getUsers.getEmail().equals(email)) {

            System.out.println("before authentication");
            //trash starts
            Authentication authenticate = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            System.out.println("after authentication");
            if(authenticate.isAuthenticated()){
                 System.out.println("login success with creds: " + authenticate.getName());
                return (ResponseEntity<?>) status(200).body("login success: " + jwtConfig.generateToken(email));
            }
            else{
                System.out.println("invalid creds");
                return (ResponseEntity<?>) status(200).body("invalid creds");   
            }
            //  System.out.println("login success: ");
            // return (ResponseEntity<?>) status(200).body("login success: " + jwtConfig.generateToken(email));

           
        } else {
             System.out.println("invalid creds");
            return (ResponseEntity<?>) status(200).body("invalid creds");
        }

    }

    @GetMapping("/validateToken")
    public Claims validateToken(@RequestParam("jwt") String jwtToken){
        System.out.println("validatetoken url called");
        return (Claims) jwtConfig.getClaims(jwtToken);
    }
}