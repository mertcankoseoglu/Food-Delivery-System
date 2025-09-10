package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.services.CourierLoginService;
import com.hudfs.hudfs28.dtos.LoginRequest;
import com.hudfs.hudfs28.dtos.CourierLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courier")
public class CourierLoginController {

    @Autowired
    private CourierLoginService authService;

    @PostMapping("/login")
    public CourierLoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}