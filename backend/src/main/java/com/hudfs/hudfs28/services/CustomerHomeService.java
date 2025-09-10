package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.CustomerMenuResponse;
import com.hudfs.hudfs28.dtos.CustomerProductResponse;
import com.hudfs.hudfs28.dtos.ProductShortInfo;
import com.hudfs.hudfs28.dtos.RestaurantInfo;
import com.hudfs.hudfs28.entities.Menu;
import com.hudfs.hudfs28.entities.Product;
import com.hudfs.hudfs28.repositories.ProductRepository;
import com.hudfs.hudfs28.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerHomeService {
    @Autowired
    private ProductRepository productRepository;

    public List<CustomerProductResponse> getHomepageProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> new CustomerProductResponse(
                        product.getProductId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getCalories(),
                        product.isInStock(),
                        new RestaurantInfo(
                                product.getRestaurant().getId(),
                                product.getRestaurant().getName(),
                                product.getRestaurant().isOpen()
                        )
                ))
                .collect(Collectors.toList());
    }

    
    @Autowired
    private MenuRepository menuRepository;

    public List<CustomerMenuResponse> getHomepageMenus() {
        List<Menu> menus = menuRepository.findAll();
    
        return menus.stream()
                .map(menu -> new CustomerMenuResponse(
                        menu.getMenuId(),
                        menu.getName(),
                        menu.getCategory(),
                        menu.getPrice(),
                        menu.getCalories(),
                        menu.isInStock(),
                        menu.getProducts().stream()
                            .map(product -> {
                                ProductShortInfo shortInfo = new ProductShortInfo();
                                shortInfo.setProductId(product.getProductId());
                                shortInfo.setName(product.getName());
                                return shortInfo;
                            })
                            .collect(Collectors.toList()),
                        new RestaurantInfo(
                                menu.getRestaurant().getId(),
                                menu.getRestaurant().getName(),
                                menu.getRestaurant().isOpen()
                        )
                ))
                .collect(Collectors.toList());
    }
    
}