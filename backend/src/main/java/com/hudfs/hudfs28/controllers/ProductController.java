package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.ProductResponse;
import com.hudfs.hudfs28.dtos.ProductInfo;
import com.hudfs.hudfs28.dtos.ProductRequest;
import com.hudfs.hudfs28.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/{restaurantId}/create")
    public ProductResponse createProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable Long restaurantId,
            @RequestBody ProductRequest request) {
        token = token.replace("Bearer ", "");
        return productService.createProduct(token, restaurantId, request);
    }

    @PutMapping("/{restaurantId}/update/{productId}")
    public ProductResponse updateProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable Long restaurantId,
            @PathVariable Integer productId,
            @RequestBody ProductRequest request) {
        token = token.replace("Bearer ", "");
        return productService.updateProduct(token, restaurantId, productId, request);
    }

    @DeleteMapping("/{restaurantId}/delete/{productId}")
    public ProductResponse deleteProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable Long restaurantId,
            @PathVariable Integer productId) {
        token = token.replace("Bearer ", "");
        return productService.deleteProduct(token, restaurantId, productId);
    }

    @PutMapping("/{restaurantId}/toggle-stock/{productId}")
    public ProductResponse toggleProductStock(
            @RequestHeader("Authorization") String token,
            @PathVariable Long restaurantId,
            @PathVariable Integer productId) {
        token = token.replace("Bearer ", "");
        return productService.toggleProductStock(token, restaurantId, productId);
    }

    @GetMapping("/{restaurantId}/products")
    public List<ProductInfo> getMenu(@RequestHeader("Authorization") String authHeader,
                                     @PathVariable Long restaurantId) {
        return productService.listProducts(authHeader, restaurantId);
    }
}