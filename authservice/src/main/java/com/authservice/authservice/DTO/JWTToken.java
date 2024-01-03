package com.authservice.authservice.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JWTToken {
    public JWTToken(String string) {
    }

    private String token;
}
