package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.Customer;
import com.hudfs.hudfs28.entities.CustomerTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface CustomerTaskRepository extends JpaRepository<CustomerTask, Long> {
    List<CustomerTask> findByCustomerCustomerId(Long customerId);
    List<CustomerTask> findByCustomer(Customer customer);
    List<CustomerTask> findByCustomerAndDeadlineBefore(Customer customer, LocalDateTime now);
    List<CustomerTask> findByCustomerAndDeadlineAfter(Customer customer, LocalDateTime now);
    List<CustomerTask> findByCustomerAndAssignedTrue(Customer customer);
    List<CustomerTask> findByCustomerAndCompletedFalse(Customer customer);
}