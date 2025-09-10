package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.RestaurantRatingRequest;
import com.hudfs.hudfs28.entities.Customer;
import com.hudfs.hudfs28.entities.Restaurant;
import com.hudfs.hudfs28.entities.RestaurantRating;
import com.hudfs.hudfs28.repositories.CustomerRepository;
import com.hudfs.hudfs28.repositories.RestaurantRatingRepository;
import com.hudfs.hudfs28.repositories.RestaurantRepository;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class RatingService {

    @Autowired private CustomerRepository customerRepo;
    @Autowired private RestaurantRepository restaurantRepo;
    @Autowired private RestaurantRatingRepository ratingRepo;

    public Map<String, Object> rateRestaurant(RestaurantRatingRequest request, String token) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        Customer customer = customerRepo.findByMail(email);

        if (customer == null) {
            return Map.of("success", false, "detail", "unauthorized");
        }

        Restaurant restaurant = restaurantRepo.findById(request.getRestaurantId()).orElse(null);
        if (restaurant == null) {
            return Map.of("success", false, "detail", "restaurant not found");
        }

        if (request.getRating() < 1 || request.getRating() > 5) {
            return Map.of("success", false, "detail", "rating must be between 1 and 5");
        }

        RestaurantRating rating = new RestaurantRating();
        rating.setCustomer(customer);
        rating.setRestaurant(restaurant);
        rating.setRating(request.getRating());
        rating.setReview(request.getReview());
        rating.setCreatedAt(LocalDateTime.now());

        ratingRepo.save(rating);

        // Ortalama puanı güncelle (opsiyonel)
        double avg = ratingRepo.findByRestaurant(restaurant)
                        .stream()
                        .mapToInt(RestaurantRating::getRating)
                        .average().orElse(0.0);

        restaurant.setOverAllRating((float) avg);
        restaurantRepo.save(restaurant);

        return Map.of("success", true, "detail", "rating saved");
    }
}