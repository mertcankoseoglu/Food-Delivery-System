package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.OrderPlacementRequest;
import com.hudfs.hudfs28.dtos.OrderStatusUpdateRequest;
import com.hudfs.hudfs28.entities.Courier;
import com.hudfs.hudfs28.entities.Customer;
import com.hudfs.hudfs28.entities.Restaurant;
import com.hudfs.hudfs28.repositories.CourierRepository;
import com.hudfs.hudfs28.repositories.CustomerRepository;
import com.hudfs.hudfs28.repositories.RestaurantRepository;
import com.hudfs.hudfs28.services.OrderService;
import com.hudfs.hudfs28.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired private CustomerRepository customerRepo;
    @Autowired private CourierRepository courierRepo;
    @Autowired private RestaurantRepository restaurantRepo;

    @PostMapping("/customer/{customerId}/order/place")
    public ResponseEntity<Map<String, Object>> placeOrder(
            @PathVariable Long customerId,
            @RequestBody OrderPlacementRequest request,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(orderService.placeOrder(customerId, request, token));
    }

    @GetMapping("/customer/{customerId}/orders/list")
    public ResponseEntity<?> getCustomerOrders(
            @PathVariable Long customerId,
            @RequestHeader("Authorization") String token
    ) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        Customer customer = customerRepo.findByMail(email);
        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "detail", "unauthorized"));
        }

        return ResponseEntity.ok(orderService.getCustomerOrders(customer));
    }

    @GetMapping("/courier/{courierId}/orders/list")
    public ResponseEntity<?> getCourierOrders(
            @PathVariable Integer courierId,
            @RequestHeader("Authorization") String token
    ) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        Courier courier = courierRepo.findByMail(email);
        if (courier == null || !courier.getCourierId().equals(courierId)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "detail", "unauthorized"));
        }

        return ResponseEntity.ok(orderService.getCourierOrders(courier));
    }

    @GetMapping("/admin/orders/list")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrdersForAdmin());
    }

    @GetMapping("/restaurant/{restaurantId}/orders/list")
    public ResponseEntity<?> getRestaurantOrders(
            @PathVariable Long restaurantId,
            @RequestHeader("Authorization") String token
    ) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        Restaurant restaurant = restaurantRepo.findByMail(email);
        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "detail", "unauthorized"));
        }

        return ResponseEntity.ok(orderService.getRestaurantOrders(restaurant));
    }

    @PutMapping("/restaurant/{restaurantId}/order/{orderId}/update-status")
    public ResponseEntity<Map<String, Object>> updateByRestaurant(
            @PathVariable Long restaurantId,
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest request,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(orderService.updateStatusByRestaurant(restaurantId, orderId, request, token));
    }

    @PutMapping("/courier/{courierId}/order/{orderId}/update-status")
    public ResponseEntity<Map<String, Object>> updateByCourier(
            @PathVariable Integer courierId,
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest request,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(orderService.updateStatusByCourier(courierId, orderId, request, token));
    }

    @PutMapping("/customer/{customerId}/order/{orderId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(
            @PathVariable Long customerId,
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(orderService.cancelOrder(customerId, orderId, token));
    }

}
