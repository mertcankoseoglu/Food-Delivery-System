package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Gerekirse özel sorgular burada tanımlanabilir
}