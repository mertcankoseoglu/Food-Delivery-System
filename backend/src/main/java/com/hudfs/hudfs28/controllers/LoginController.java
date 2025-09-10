package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.services.LoginService;
import com.hudfs.hudfs28.dtos.LoginRequest;
import com.hudfs.hudfs28.dtos.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurant")
public class LoginController {

    @Autowired
    private LoginService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
