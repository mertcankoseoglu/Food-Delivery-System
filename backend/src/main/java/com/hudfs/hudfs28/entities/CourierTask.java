package com.hudfs.hudfs28.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "courier_tasks") 
public class CourierTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courierTaskId;

    private Integer requiredDeliveries;
    private Integer rewardPoints;
    private Integer progress;
    private boolean completed;
    
    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;
    
    public CourierTask() {
    }
    
    public CourierTask(Integer requiredDeliveries, Integer rewardPoints, Courier courier) {
        this.requiredDeliveries = requiredDeliveries;
        this.rewardPoints = rewardPoints;
        this.courier = courier;
        this.progress = 0;
        this.completed = false;
    }

    // Getters and Setters
    public Long getId() {
        return courierTaskId;
    }
    
    public void setId(Long courierTaskId) {
        this.courierTaskId = courierTaskId;
    }
    
    public int getRequiredDeliveries() {
        return requiredDeliveries;
    }
    
    public void setRequiredDeliveries(Integer requiredDeliveries) {
        this.requiredDeliveries = requiredDeliveries;
    }
    
    public int getRewardPoints() {
        return rewardPoints;
    }
    
    public void setRewardPoints(Integer rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
    
    public int getProgress() {
        return progress;
    }
    
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public Courier getCourier() {
        return courier;
    }
    
    public void setCourier(Courier courier) {
        this.courier = courier;
    }
}
