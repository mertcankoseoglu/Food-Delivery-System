package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.CartAddRequest;
import com.hudfs.hudfs28.dtos.CartUpdateRequest;
import com.hudfs.hudfs28.services.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CartController {

    @Autowired private CartService cartService;

    @GetMapping("/{customerId}/cart_items")
    public ResponseEntity<Map<String, Object>> getCartItems(
            @PathVariable Long customerId,
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok(cartService.getCartItems(customerId, authHeader));
    }

    @PostMapping("/{customerId}/cart/add")
    public ResponseEntity<Map<String, String>> addToCart(
            @PathVariable Long customerId,
            @RequestBody CartAddRequest request,
            @RequestHeader("Authorization") String authHeader
    ) 
    {
        return ResponseEntity.ok(cartService.addToCart(customerId, request, authHeader));
    }

    @PutMapping("/{customerId}/cartItem/{cartItemId}/update_number")
    public ResponseEntity<Map<String, String>> updateNumber(
            @PathVariable Long customerId,
            @PathVariable Long cartItemId,
            @RequestBody CartUpdateRequest request,
            @RequestHeader("Authorization") String authHeader
    ) 
    {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(customerId, cartItemId, request, authHeader));
    }

}
