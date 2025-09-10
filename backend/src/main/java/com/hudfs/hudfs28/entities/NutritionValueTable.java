package com.hudfs.hudfs28.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "nutritionValueTable")
public class NutritionValueTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nutritionTableId;

    private Float carbohydrate;
    private Float protein;
    private Float fat;
    private Float cholesterol;
    private Float sugar;
    private Float salt;
    private Float water;
}