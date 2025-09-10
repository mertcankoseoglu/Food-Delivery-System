package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByCourier(Courier courier);
    List<Order> findAll(); 
}
