package com.ApiGateway.ApiGateway.Config;

    
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Configuration
public class JWTConfig {
    
    private final String secret_key = "GY$^@#*@7569%^&@%$^#&$^#&$#^$&*#(JHC$#*(($&$&$&$&$&$&^#%$&^&";

    //generate token
    public String generateToken(String subject){
        return Jwts.builder()
                   .setSubject(subject)
                   .setIssuer("Microservice Security")
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20)))
                   .signWith(SignatureAlgorithm.HS256, secret_key.getBytes())
                   .compact();
    }

     //get claims
    public Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(secret_key.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    //read expirydate
    public Date getExpDate(String token){
        return getClaims(token).getExpiration();
    }

    //read username/subject
    public String getUserName(String token){
        return getClaims(token).getSubject();
    }

    //validate expiration date
    public boolean isTokenExp(String token){
        Date expDate = getExpDate(token);
        return expDate.before(new Date(System.currentTimeMillis()));
    }


     //validate token (We need to check the DB user and token user is same and token is not exp)
     public boolean validateToken(String token, String DBUserName){
        String tokenUsername = getUserName(token);
        System.out.println("inside token validate menthod starts " + tokenUsername + "DbUSER " + DBUserName);
        if(tokenUsername.equals(DBUserName) && !isTokenExp(token)){
            System.out.println("inside token validate menthod true");

            return true;
        }
        else{
            System.out.println("inside token validate menthod false");

            return false;
        }
    }
}
