package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    boolean existsByMail(String mail);
    Restaurant findByMail(String mail);
}