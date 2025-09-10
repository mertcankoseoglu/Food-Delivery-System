package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByMail(String mail);
    Customer findByMail(String mail);
}
