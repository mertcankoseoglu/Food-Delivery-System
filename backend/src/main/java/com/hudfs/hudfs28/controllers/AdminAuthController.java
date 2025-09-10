package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.AdminLoginRequest;
import com.hudfs.hudfs28.dtos.AdminLoginResponse;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    private final String adminUsername = "admin";
    private final String adminPassword = "admin123";

    @PostMapping("/login")
    public AdminLoginResponse login(@RequestBody AdminLoginRequest request) {
        if (adminUsername.equals(request.getUsername()) && adminPassword.equals(request.getPassword())) {
            String token = JwtUtil.generateToken(request.getUsername()); // generate token based on username
            return new AdminLoginResponse(true, token, "Admin login successful");
        } else {
            return new AdminLoginResponse(false, null, "Invalid username or password");
        }
    }
}
