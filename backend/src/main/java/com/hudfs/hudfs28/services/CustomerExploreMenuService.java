package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.*;
import com.hudfs.hudfs28.entities.*;
import com.hudfs.hudfs28.repositories.*;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerExploreMenuService {

    @Autowired private CustomerRepository customerRepo;
    @Autowired private MenuRepository menuRepo;
    @Autowired private CustomerLikesMenuRepository likesMenuRepo;

    public List<ExploreMenuDTO> getExploreMenus(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String tokenEmail = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepo.findByMail(tokenEmail);

        if (customer == null) throw new RuntimeException("Invalid or expired token");

        List<ExploreMenuDTO> exploreMenus = new ArrayList<>();

        for (Menu menu : menuRepo.findAll()) {
            int likeCount = likesMenuRepo.countByMenuAndReaction(menu, ReactionType.LIKE);
            int dislikeCount = likesMenuRepo.countByMenuAndReaction(menu, ReactionType.DISLIKE);

            List<ProductShortInfo> productDTOs = new ArrayList<>();
            for (Product product : menu.getProducts()) {
                productDTOs.add(new ProductShortInfo(product.getProductId(), product.getName()));
            }

            Restaurant restaurant = menu.getRestaurant();
            RestaurantInfo restaurantInfo = new RestaurantInfo(restaurant.getId(), restaurant.getName(), restaurant.isOpen());

            ExploreMenuDTO dto = new ExploreMenuDTO(
                    menu.getMenuId(),
                    menu.getName(),
                    menu.getCategory(),
                    menu.getPrice().floatValue(),       
                    menu.getCalories().floatValue(),
                    menu.isInStock(),
                    likeCount,
                    dislikeCount,
                    productDTOs,
                    restaurantInfo
            );

            exploreMenus.add(dto);
        }

        return exploreMenus;
    }
}