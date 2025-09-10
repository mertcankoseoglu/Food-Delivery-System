package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    Optional<CartItem> findByCartAndMenu(Cart cart, Menu menu);
}
