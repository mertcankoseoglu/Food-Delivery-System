package com.hudfs.hudfs28.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_likes_menu")
public class CustomerLikesMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Menu menu;

    @Enumerated(EnumType.STRING)
    private ReactionType reaction;

    public CustomerLikesMenu() {}

    public CustomerLikesMenu(Customer customer, Menu menu, ReactionType reaction) {
        this.customer = customer;
        this.menu = menu;
        this.reaction = reaction;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Menu getMenu() { return menu; }
    public void setMenu(Menu menu) { this.menu = menu; }

    public ReactionType getReactionType() { return reaction; }
    public void setReactionType(ReactionType reaction) {this.reaction = reaction; }
}
