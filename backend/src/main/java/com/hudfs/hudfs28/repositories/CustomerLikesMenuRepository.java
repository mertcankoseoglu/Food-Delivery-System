package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerLikesMenuRepository extends JpaRepository<CustomerLikesMenu, Long> {
    CustomerLikesMenu findByCustomerAndMenu(Customer customer, Menu menu);
    void deleteByCustomerAndMenu(Customer customer, Menu menu);
    int countByMenuAndReaction(Menu menu, ReactionType reaction);
}
