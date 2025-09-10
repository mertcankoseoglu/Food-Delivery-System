package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.Coupon;
import com.hudfs.hudfs28.entities.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.List;

@RepositoryRestResource
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    Optional<Coupon> findByCustomerAndCode(Customer customer, String code);
    List<Coupon> findByCustomer(Customer customer);
}