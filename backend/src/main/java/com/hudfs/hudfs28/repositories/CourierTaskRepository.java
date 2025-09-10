package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.CourierTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CourierTaskRepository extends JpaRepository<CourierTask, Long> {
}