package com.hudfs.hudfs28.controllers;

import com.hudfs.hudfs28.dtos.CustomerProductResponse;
import com.hudfs.hudfs28.dtos.CustomerMenuResponse;
import com.hudfs.hudfs28.services.CustomerHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customer/homepage")
public class CustomerHomeController {
    @Autowired
    private CustomerHomeService customerHomeService;

    @GetMapping("/products")
    public List<CustomerProductResponse> products() {
        return customerHomeService.getHomepageProducts();
    }

    @GetMapping("/menus")
    public List<CustomerMenuResponse> menus() {
        return customerHomeService.getHomepageMenus();
    }
}
