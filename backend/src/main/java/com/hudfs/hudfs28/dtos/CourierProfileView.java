package com.hudfs.hudfs28.dtos;

public class CourierProfileView {
    private Integer courierId;
    private String name;
    private Integer age;
    private String telephoneNumber;
    private String mail;
    private Float level;
    private Float points;
    private boolean available;

    // Getters and setters
    public Integer getCourierId() {
        return courierId;
    }
    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Float getLevel() {
        return level;
    }
    public void setLevel(Float level) {
        this.level = level;
    }

    public Float getPoints() {
        return points;
    }
    public void setPoints(Float points) {
        this.points = points;
    }
}