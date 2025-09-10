package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.AddressRequest;
import com.hudfs.hudfs28.dtos.RestaurantProfileData;
import com.hudfs.hudfs28.dtos.RestaurantProfileResponse;
import com.hudfs.hudfs28.dtos.RestaurantRequest;
import com.hudfs.hudfs28.dtos.RestaurantResponse;
import com.hudfs.hudfs28.dtos.RestaurantUpdateRequest;
import com.hudfs.hudfs28.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/register")
    public RestaurantResponse register(@RequestBody RestaurantRequest request) {
        return restaurantService.register(request);
    }

    @GetMapping("/{restaurantId}/profile")
    public RestaurantProfileData getProfile(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long restaurantId) {
        String token = authHeader.replace("Bearer ", "");
        return restaurantService.getProfile(token, restaurantId);
    }

    @PutMapping("/{restaurantId}/profile/update")
    public RestaurantProfileResponse updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long restaurantId,
            @RequestBody RestaurantUpdateRequest request) {
        String token = authHeader.replace("Bearer ", "");
        return restaurantService.updateProfile(token, restaurantId, request);
    }

    @PostMapping("/{restaurantId}/profile/add-address")
    public RestaurantProfileResponse addAddress(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long restaurantId,
            @RequestBody AddressRequest request) {
        String token = authHeader.replace("Bearer ", "");
        return restaurantService.addAddress(token, restaurantId, request);
    }

    @PutMapping("/{restaurantId}/profile/update-address/{addressId}")
    public RestaurantProfileResponse updateAddress(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long restaurantId,
            @PathVariable Long addressId,
            @RequestBody AddressRequest request) {
        String token = authHeader.replace("Bearer ", "");
        return restaurantService.updateAddress(token, restaurantId, addressId, request);
    }

    @DeleteMapping("/{restaurantId}/profile/delete-address/{addressId}")
    public RestaurantProfileResponse deleteAddress(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long restaurantId,
            @PathVariable Long addressId) {
        String token = authHeader.replace("Bearer ", "");
        return restaurantService.deleteAddress(token, restaurantId, addressId);
    }

    @PutMapping("/{restaurantId}/status-toggle")
    public RestaurantProfileResponse toggleStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long restaurantId) {
        token = token.replace("Bearer ", "");
        return restaurantService.toggleRestaurantStatus(token, restaurantId);
    }

    @GetMapping("/{restaurantId}/profile/rates-and-reviews")
    public ResponseEntity<?> getRatesAndReviews(@PathVariable Long restaurantId) {
        return restaurantService.getRestaurantRatings(restaurantId);
    }
}
