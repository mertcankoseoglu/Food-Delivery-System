package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.ProductInfo;
import com.hudfs.hudfs28.dtos.ProductRequest;
import com.hudfs.hudfs28.dtos.ProductResponse;
import com.hudfs.hudfs28.entities.Menu;
import com.hudfs.hudfs28.entities.Product;
import com.hudfs.hudfs28.entities.Restaurant;
import com.hudfs.hudfs28.repositories.ProductRepository;
import com.hudfs.hudfs28.repositories.RestaurantRepository;
import com.hudfs.hudfs28.repositories.MenuRepository;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;
    
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public ProductResponse createProduct(String token, Long restaurantId, ProductRequest request) {
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return new ProductResponse(false, "Unauthorized.");
        }

        // Validation: Check if any part is missing
        if (isNullOrEmpty(request.getName()) ||
            isNullOrEmpty(request.getCategory()) ||
            request.getPrice() == null ||
            request.getCalories() == null) {
            return new ProductResponse(false, "All parts must be filled.");
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setCalories(request.getCalories());
        product.setRestaurant(restaurant);

        productRepository.save(product);
        return new ProductResponse(true, "Product created successfully.");
    }

    public ProductResponse updateProduct(String token, Long restaurantId, Integer productId, ProductRequest request) {
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return new ProductResponse(false, "Unauthorized.");
        }

        Optional<Product> optionalProduct = productRepository.findByProductIdAndRestaurantRestaurantId(productId, restaurantId);
        if (optionalProduct.isEmpty()) {
            return new ProductResponse(false, "Product not found or you are not the owner.");
        }

        // Validation: Check if any part is missing
        if (isNullOrEmpty(request.getName()) ||
            isNullOrEmpty(request.getCategory()) ||
            request.getPrice() == null ||
            request.getCalories() == null) {
            return new ProductResponse(false, "All parts must be filled.");
        }

        Product product = optionalProduct.get();

        // Check if there is NO change
        if (product.getName().equals(request.getName()) &&
            product.getCategory().equals(request.getCategory()) &&
            product.getPrice().equals(request.getPrice()) &&
            product.getCalories().equals(request.getCalories())) {
            return new ProductResponse(false, "There is no change.");
        }

        // Update
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setCalories(request.getCalories());

        productRepository.save(product);

        // Update calories of all menus that include this product
        List<Menu> affectedMenus = menuRepository.findByProductsContaining(product);
        for (Menu menu : affectedMenus) {
            float totalCalories = 0f;
            for (Product p : menu.getProducts()) {
                totalCalories += (p.getCalories() != null) ? p.getCalories() : 0f;
            }
            menu.setCalories(totalCalories);
            menuRepository.save(menu);
        }
        return new ProductResponse(true, "Product updated successfully.");
    }

    public ProductResponse deleteProduct(String token, Long restaurantId, Integer productId) {
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return new ProductResponse(false, "Unauthorized.");
        }

        Optional<Product> optionalProduct = productRepository.findByProductIdAndRestaurantRestaurantId(productId, restaurantId);
        if (optionalProduct.isEmpty()) {
            return new ProductResponse(false, "Product not found or you are not the owner.");
        }

        productRepository.delete(optionalProduct.get());
        return new ProductResponse(true, "Product deleted successfully.");
    }

    public List<ProductInfo> listProducts(String authHeader, Long restaurantId) {
        String token = authHeader.replace("Bearer ", ""); // Remove Bearer prefix
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return List.of(); // return empty list if unauthorized
        }

        List<Product> products = productRepository.findByRestaurantRestaurantId(restaurantId);

        return products.stream()
                .map(product -> new ProductInfo(
                        product.getProductId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getCalories(),
                        product.isInStock()
                ))
                .collect(Collectors.toList());
    }

    public ProductResponse toggleProductStock(String token, Long restaurantId, Integer productId) {
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return new ProductResponse(false, "Unauthorized.");
        }

        Optional<Product> productOpt = productRepository.findByProductIdAndRestaurantRestaurantId(productId, restaurantId);
        if (productOpt.isEmpty()) {
            return new ProductResponse(false, "Product not found.");
        }

        Product product = productOpt.get();

        // Toggle product stock
        boolean newStockStatus = !product.isInStock();
        product.setInStock(newStockStatus);
        productRepository.save(product);

        // Tüm menülerdeki durumu kontrol et
        List<Menu> affectedMenus = menuRepository.findByProductsContaining(product);
        for (Menu menu : affectedMenus) {
            boolean allProductsInStock = menu.getProducts().stream().allMatch(Product::isInStock);
            menu.setInStock(allProductsInStock);
            menuRepository.save(menu);
        }

        return new ProductResponse(true,
                product.isInStock()
                        ? "Product is now IN STOCK. Menus updated accordingly."
                        : "Product is now OUT OF STOCK. Menus updated accordingly.");
    }

}