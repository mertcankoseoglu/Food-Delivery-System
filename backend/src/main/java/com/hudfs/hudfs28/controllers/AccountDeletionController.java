package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.entities.Restaurant;
import com.hudfs.hudfs28.entities.Customer;
import com.hudfs.hudfs28.entities.Courier;
import com.hudfs.hudfs28.repositories.RestaurantRepository;
import com.hudfs.hudfs28.repositories.CustomerRepository;
import com.hudfs.hudfs28.repositories.CourierRepository;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountDeletionController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CourierRepository courierRepository;

    // Restaurant Account Delete
    @DeleteMapping("/restaurant/{restaurantId}/delete")
    public Map<String, Object> deleteRestaurant(@PathVariable Long restaurantId, @RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            String emailFromToken = JwtUtil.validateTokenAndGetEmail(token);

            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

            if (restaurant == null) {
                response.put("success", false);
                response.put("detail", "Restaurant not found.");
                return response;
            }

            if (!restaurant.getMail().equals(emailFromToken)) {
                response.put("success", false);
                response.put("detail", "Unauthorized to delete this restaurant account.");
                return response;
            }

            restaurantRepository.delete(restaurant);

            response.put("success", true);
            response.put("detail", "Restaurant account deleted successfully.");
            return response;

        } catch (Exception e) {
            response.put("success", false);
            response.put("detail", "Invalid token or error occurred.");
            return response;
        }
    }

    // Customer Account Delete
    @DeleteMapping("/customer/{customerId}/delete")
    public Map<String, Object> deleteCustomer(@PathVariable Long customerId, @RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            String emailFromToken = JwtUtil.validateTokenAndGetEmail(token);

            Customer customer = customerRepository.findById(customerId).orElse(null);

            if (customer == null) {
                response.put("success", false);
                response.put("detail", "Customer not found.");
                return response;
            }

            if (!customer.getMail().equals(emailFromToken)) {
                response.put("success", false);
                response.put("detail", "Unauthorized to delete this customer account.");
                return response;
            }

            customerRepository.delete(customer);

            response.put("success", true);
            response.put("detail", "Customer account deleted successfully.");
            return response;

        } catch (Exception e) {
            response.put("success", false);
            response.put("detail", "Invalid token or error occurred.");
            return response;
        }
    }

    // Courier Account Delete
    @DeleteMapping("/courier/{courierId}/delete")
    public Map<String, Object> deleteCourier(@PathVariable Integer courierId, @RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            String emailFromToken = JwtUtil.validateTokenAndGetEmail(token);

            Courier courier = courierRepository.findById(courierId).orElse(null);

            if (courier == null) {
                response.put("success", false);
                response.put("detail", "Courier not found.");
                return response;
            }

            if (!courier.getMail().equals(emailFromToken)) {
                response.put("success", false);
                response.put("detail", "Unauthorized to delete this courier account.");
                return response;
            }

            courierRepository.delete(courier);

            response.put("success", true);
            response.put("detail", "Courier account deleted successfully.");
            return response;

        } catch (Exception e) {
            response.put("success", false);
            response.put("detail", "Invalid token or error occurred.");
            return response;
        }
    }
}
