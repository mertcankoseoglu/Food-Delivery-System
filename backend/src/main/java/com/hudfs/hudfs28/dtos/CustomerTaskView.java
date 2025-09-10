package com.hudfs.hudfs28.dtos;

import java.time.LocalDateTime;

public class CustomerTaskView {
    private Long taskId;
    private String taskType; // "PRODUCT" or "MENU"
    private String targetName; // product or menu name
    private Integer requiredQuantity;
    private Integer progress;
    private Boolean completed;
    private Integer rewardPoints;
    private LocalDateTime deadline;

    public CustomerTaskView() {}

    public CustomerTaskView(Long taskId, String taskType, String targetName, Integer requiredQuantity,
                Integer progress, Boolean completed, Integer rewardPoints, LocalDateTime deadline) {
        this.taskId = taskId;
        this.taskType = taskType;
        this.targetName = targetName;
        this.requiredQuantity = requiredQuantity;
        this.progress = progress;
        this.completed = completed;
        this.rewardPoints = rewardPoints;
        this.deadline = deadline;
    }

    // Getters and setters
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Integer getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(Integer requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
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

    public Integer getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(Integer rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
