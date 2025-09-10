package com.hudfs.hudfs28.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer couponId;

    @ManyToOne
    private Customer customer;

    private Float discountAmount;
    private String code;
    private Date expiryDate;
    private Float minimumOrderAmount;
}