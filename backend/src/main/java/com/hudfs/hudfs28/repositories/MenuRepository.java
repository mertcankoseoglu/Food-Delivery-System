package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.Menu;
import com.hudfs.hudfs28.entities.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByMenuIdAndRestaurantRestaurantId(Long menuId, Long restaurantId);
    List<Menu> findByRestaurantRestaurantId(Long restaurantId);
    List<Menu> findByProductsContaining(Product product);
}