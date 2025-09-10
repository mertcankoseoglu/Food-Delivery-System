package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.Customer;
import com.hudfs.hudfs28.entities.Restaurant;
import com.hudfs.hudfs28.entities.RestaurantRating;
import com.hudfs.hudfs28.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRatingRepository extends JpaRepository<RestaurantRating, Long> {
    List<RestaurantRating> findByRestaurant(Restaurant restaurant);
    Optional<RestaurantRating> findByCustomerAndOrder(Customer customer, Order order);
}