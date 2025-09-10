package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.LoginRequest;
import com.hudfs.hudfs28.dtos.CourierLoginResponse;
import com.hudfs.hudfs28.entities.Courier;
import com.hudfs.hudfs28.repositories.CourierRepository;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourierLoginService {

    @Autowired
    private CourierRepository courierRepository;

    public CourierLoginResponse login(LoginRequest request) {
        Courier courier = courierRepository.findByMail(request.getMail());

        if (courier == null || !courier.getPassword().equals(request.getPassword())) {
            return new CourierLoginResponse(false, null, null, "Invalid email or password.");
        }

        String token = JwtUtil.generateToken(courier.getMail());

        return new CourierLoginResponse(true, token, courier.getCourierId(), "Login successful.");
    }
}
