package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.RestaurantRatingRequest;
import com.hudfs.hudfs28.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping("/restaurant")
    public ResponseEntity<Map<String, Object>> rateRestaurant(
            @RequestBody RestaurantRatingRequest request,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(ratingService.rateRestaurant(request, token));
    }
}
