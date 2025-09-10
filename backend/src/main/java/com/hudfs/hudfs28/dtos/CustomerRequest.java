package com.hudfs.hudfs28.dtos;

public class CustomerRequest {
    private String name;
    private String mail;
    private String password;
    private String passwordVerification;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPasswordVerification() { return passwordVerification; }
    public void setPasswordVerification(String passwordVerification) { this.passwordVerification = passwordVerification; }
}
