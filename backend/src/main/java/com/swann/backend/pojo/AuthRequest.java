package com.swann.backend.pojo;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}