package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.MenuInfo;
import com.hudfs.hudfs28.dtos.MenuRequest;
import com.hudfs.hudfs28.dtos.MenuResponse;
import com.hudfs.hudfs28.dtos.MenuUpdateRequest;
import com.hudfs.hudfs28.dtos.ProductResponse;
import com.hudfs.hudfs28.dtos.ProductShortInfo;
import com.hudfs.hudfs28.dtos.RestaurantInfo;
import com.hudfs.hudfs28.entities.Menu;
import com.hudfs.hudfs28.entities.Product;
import com.hudfs.hudfs28.entities.Restaurant;
import com.hudfs.hudfs28.repositories.MenuRepository;
import com.hudfs.hudfs28.repositories.ProductRepository;
import com.hudfs.hudfs28.repositories.RestaurantRepository;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ProductRepository productRepository;

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public MenuResponse createMenu(String token, Long restaurantId, MenuRequest request) {
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return new MenuResponse(false, "Unauthorized.");
        }

        if (isNullOrEmpty(request.getName()) || isNullOrEmpty(request.getCategory()) || 
            request.getPrice() == null || request.getProducts() == null || request.getProducts().isEmpty()) {
            return new MenuResponse(false, "All parts must be filled.");
        }

        List<Product> products = new ArrayList<>();
        float totalCalories = 0f;

        for (ProductShortInfo p : request.getProducts()) {
            Optional<Product> productOptional = productRepository.findById(p.getProductId());
            if (productOptional.isEmpty()) {
                return new MenuResponse(false, "One of the products not found.");
            }
            Product product = productOptional.get();
            products.add(product);
            totalCalories += product.getCalories();
        }

        Menu menu = new Menu();
        menu.setName(request.getName());
        menu.setCategory(request.getCategory());
        menu.setPrice(request.getPrice());
        menu.setCalories(totalCalories);
        menu.setRestaurant(restaurant);
        menu.setProducts(products);

        menuRepository.save(menu);

        return new MenuResponse(true, "Menu created successfully.");
    }

    public ProductResponse updateMenu(String token, Long restaurantId, Long menuId, MenuUpdateRequest request) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(email);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return new ProductResponse(false, "Unauthorized.");
        }

        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if (optionalMenu.isEmpty()) {
            return new ProductResponse(false, "Menu not found.");
        }

        Menu menu = optionalMenu.get();

        if (!menu.getRestaurant().getId().equals(restaurantId)) {
            return new ProductResponse(false, "Unauthorized: You do not have permission to update the menu.");
        }

        if (isNullOrEmpty(request.getName()) || isNullOrEmpty(request.getCategory()) || request.getPrice() == null) {
            return new ProductResponse(false, "All fields must be filled.");
        }

        if (
                menu.getName().equals(request.getName()) &&
                menu.getCategory().equals(request.getCategory()) &&
                menu.getPrice().equals(request.getPrice())
        ) {
            return new ProductResponse(false, "There is no change.");
        }

        // Update
        menu.setName(request.getName());
        menu.setCategory(request.getCategory());
        menu.setPrice(request.getPrice());

        menuRepository.save(menu);
        return new ProductResponse(true, "Menu updated successfully.");
    }
    
    public MenuResponse deleteMenu(String token, Long restaurantId, Long menuId) {
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);
    
        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return new MenuResponse(false, "Unauthorized.");
        }
    
        Optional<Menu> menuOpt = menuRepository.findById(menuId);
        if (menuOpt.isEmpty()) {
            return new MenuResponse(false, "Menu not found.");
        }
    
        Menu menu = menuOpt.get();
        if (!menu.getRestaurant().getId().equals(restaurantId)) {
            return new MenuResponse(false, "Unauthorized.");
        }
    
        menuRepository.delete(menu);
        return new MenuResponse(true, "Menu deleted successfully.");
    }

    public List<MenuInfo> listMenus(String authHeader, Long restaurantId) {
        String token = authHeader.replace("Bearer ", "");
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return List.of(); // return empty list if unauthorized
        }

        List<Menu> menus = menuRepository.findByRestaurantRestaurantId(restaurantId);

        return menus.stream()
                .map(menu -> new MenuInfo(
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