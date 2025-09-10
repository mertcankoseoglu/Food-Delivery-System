package com.hudfs.hudfs28.repositories;

import com.hudfs.hudfs28.entities.NutritionValueTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface NutritionValueTableRepository extends JpaRepository<NutritionValueTable, Integer> {
    // Gerekirse özel sorgular burada tanımlanabilir
}