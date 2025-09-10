package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.CustomerLoginResponse;
import com.hudfs.hudfs28.dtos.LoginRequest;
import com.hudfs.hudfs28.entities.Customer;
import com.hudfs.hudfs28.repositories.CustomerRepository;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerLoginService {

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerLoginResponse login(LoginRequest request) {
        Customer customer = customerRepository.findByMail(request.getMail());

        if (customer == null || !customer.getPassword().equals(request.getPassword())) {
            return new CustomerLoginResponse(false, null, null, "Invalid mail or password.");
        }

        String token = JwtUtil.generateToken(customer.getMail());

        return new CustomerLoginResponse(true, token, customer.getCustomerId(), "Login successful.");
    }
}
