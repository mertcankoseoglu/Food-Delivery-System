package com.hudfs.hudfs28.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "branch")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer branchId;

    private String telephoneNumber;
    private Float rating;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String workDays;

    @ManyToOne
    private Address address;
}