package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.LoginRequest;
import com.hudfs.hudfs28.dtos.LoginResponse;
import com.hudfs.hudfs28.entities.Restaurant;
import com.hudfs.hudfs28.repositories.RestaurantRepository;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public LoginResponse login(LoginRequest request) {
        Restaurant restaurant = restaurantRepository.findByMail(request.getMail());

        if (restaurant == null || !restaurant.getPassword().equals(request.getPassword())) {
            return new LoginResponse(false, null, null, "Invalid mail or password.");
        }

        String token = JwtUtil.generateToken(restaurant.getMail());
        return new LoginResponse(true, token, restaurant.getId(), "Login successful.");
    }
}

