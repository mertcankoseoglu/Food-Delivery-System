package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.entities.Customer;
import com.hudfs.hudfs28.entities.Restaurant;
import com.hudfs.hudfs28.entities.Courier;
import com.hudfs.hudfs28.repositories.RestaurantRepository;
import com.hudfs.hudfs28.repositories.CustomerRepository;
import com.hudfs.hudfs28.repositories.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminManagementController {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CourierRepository courierRepository;

    @GetMapping("/restaurants")
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/couriers")
    public List<Courier> getAllCouriers() {
        return courierRepository.findAll();
    }


    private Map<String, Object> buildResponse(boolean success, String detail) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("detail", detail);
        return response;
    }


    @DeleteMapping("/delete/restaurant/{restaurantId}")
    @Transactional
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            return ResponseEntity.status(404).body(buildResponse(false, "Restaurant not found."));
        }
        restaurantRepository.deleteById(restaurantId);
        return ResponseEntity.ok(buildResponse(true, "Restaurant deleted successfully."));
    }

    @DeleteMapping("/delete/customer/{customerId}")
    @Transactional
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            return ResponseEntity.status(404).body(buildResponse(false, "Customer not found."));
        }
        customerRepository.deleteById(customerId);
        return ResponseEntity.ok(buildResponse(true, "Customer deleted successfully."));
    }

    @DeleteMapping("/delete/courier/{courierId}")
    @Transactional
    public ResponseEntity<?> deleteCourier(@PathVariable Integer courierId) {
        if (!courierRepository.existsById(courierId)) {
            return ResponseEntity.status(404).body(buildResponse(false, "Courier not found."));
        }
        courierRepository.deleteById(courierId);
        return ResponseEntity.ok(buildResponse(true, "Courier deleted successfully."));
    }

}
