package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.services.CustomerLoginService;
import com.hudfs.hudfs28.dtos.LoginRequest;
import com.hudfs.hudfs28.dtos.CustomerLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerLoginController {

    @Autowired
    private CustomerLoginService authService;

    @PostMapping("/login")
    public CustomerLoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}