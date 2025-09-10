package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerLikesProductRepository extends JpaRepository<CustomerLikesProduct, Long> {
    CustomerLikesProduct findByCustomerAndProduct(Customer customer, Product product);
    void deleteByCustomerAndProduct(Customer customer, Product product);
}
