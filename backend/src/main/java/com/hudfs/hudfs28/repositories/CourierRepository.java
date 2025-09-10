package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Integer> {
    Courier findByMail(String mail);
    boolean existsByMail(String mail);
    Optional<Courier> findFirstByAvailableTrue();
}
