package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    // Gerekirse özel sorgular burada tanımlanabilir
}