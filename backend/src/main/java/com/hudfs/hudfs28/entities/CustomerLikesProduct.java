package com.hudfs.hudfs28.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_likes_product")
public class CustomerLikesProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Product product;

    @Enumerated(EnumType.STRING)
    private ReactionType reaction;

    public CustomerLikesProduct() {}

    public CustomerLikesProduct(Customer customer, Product product, ReactionType reaction) {
        this.customer = customer;
        this.product = product;
        this.reaction = reaction;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public ReactionType getReactionType() { return reaction; }
    public void setReactionType(ReactionType reaction) {this.reaction = reaction; }
}
