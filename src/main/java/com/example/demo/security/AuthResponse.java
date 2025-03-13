package com.example.demo.security;

import java.util.Date;

//S3
public record AuthResponse(
        String token,
        Date expiration
) {
}
