package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;



public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductIdAndRestaurantRestaurantId(Integer productId, Long restaurantId);
    List<Product> findByRestaurantRestaurantId(Long restaurantId);
}