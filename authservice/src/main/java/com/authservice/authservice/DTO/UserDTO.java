package com.authservice.authservice.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;

    private String name;
    private String email;
    private String password;
    public Object getStatusCode() {
        return null;
    }
}
