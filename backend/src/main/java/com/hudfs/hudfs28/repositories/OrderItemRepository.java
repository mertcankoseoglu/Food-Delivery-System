package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
