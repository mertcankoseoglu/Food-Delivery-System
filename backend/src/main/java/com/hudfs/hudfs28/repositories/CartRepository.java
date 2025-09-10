package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByCustomer(Customer customer);
}
