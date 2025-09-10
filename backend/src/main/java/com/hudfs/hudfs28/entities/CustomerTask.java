package com.hudfs.hudfs28.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_tasks")
public class CustomerTask {
    public enum TaskType {
        PRODUCT,
        MENU
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerTaskId;
    
    private Integer requiredQuantity;
    private Integer rewardPoints;
    private Integer progress = 0;
    private Boolean completed = false;
    
    private LocalDateTime deadline;
    
    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; // nullable
    
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu; // nullable

    @Column(nullable = false)
    private Boolean assigned = false;
    
    // Constructors
    public CustomerTask() {}
    
    public CustomerTask(TaskType taskType, Product product, Menu menu, Integer requiredQuantity, Integer rewardPoints, LocalDateTime deadline, Customer customer) {
        this.taskType = taskType;
        this.product = product;
        this.menu = menu;
        this.requiredQuantity = requiredQuantity;
        this.rewardPoints = rewardPoints;
        this.deadline = deadline;
        this.customer = customer;
        this.progress = 0;
        this.completed = false;
    }

    
    // Getters and Setters
    public Long getCustomerTaskId() {
        return customerTaskId;
    }
    
    public void setCustomerTaskId(Long customerTaskId) {
        this.customerTaskId = customerTaskId;
    }
    
    public Integer getRequiredQuantity() {
        return requiredQuantity;
    }
    
    public void setRequiredQuantity(Integer requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }
    
    public Integer getRewardPoints() {
        return rewardPoints;
    }
    
    public void setRewardPoints(Integer rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
    
    public Integer getProgress() {
        return progress;
    }
    
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
    
    public Boolean getCompleted() {
        return completed;
    }
    
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
    public LocalDateTime getDeadline() {
        return deadline;
    }
    
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
    
    public TaskType getTaskType() {
        return taskType;
    }
    
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public Menu getMenu() {
        return menu;
    }
    
    public void setMenu(Menu menu) {
        this.menu = menu;
    } 
    
    public Boolean getAssigned() {
        return assigned;
    }

    public void setAssigned(Boolean assigned) {
        this.assigned = assigned;
    }
}
