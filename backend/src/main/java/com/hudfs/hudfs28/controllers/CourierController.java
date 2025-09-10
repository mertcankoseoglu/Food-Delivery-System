package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.CourierProfileUpdateRequest;
import com.hudfs.hudfs28.dtos.CourierProfileUpdateResponse;
import com.hudfs.hudfs28.dtos.CourierRequest;
import com.hudfs.hudfs28.dtos.CourierResponse;
import com.hudfs.hudfs28.dtos.CourierProfileView;
import com.hudfs.hudfs28.services.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courier")
public class CourierController {

    @Autowired
    private CourierService courierService;

    @PostMapping("/register")
    public CourierResponse register(@RequestBody CourierRequest request) {
        return courierService.register(request);
    }

    @PutMapping("/{courierId}/profile/update")
    public CourierProfileUpdateResponse updateProfile(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer courierId,
            @RequestBody CourierProfileUpdateRequest request) {
        token = token.replace("Bearer ", "");
        return courierService.updateProfile(token, courierId, request);
    }

    @PutMapping("/{courierId}/status-toggle")
    public CourierProfileUpdateResponse toggleAvailability(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer courierId) {
        token = token.replace("Bearer ", "");
        return courierService.toggleAvailability(token, courierId);
    }

    @GetMapping("/{courierId}/profile")
    public CourierProfileView getProfile(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer courierId) {
        String token = authHeader.replace("Bearer ", "");
        return courierService.viewProfile(token, courierId);
    }
}
