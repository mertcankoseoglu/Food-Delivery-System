package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.AddressRequest;
import com.hudfs.hudfs28.dtos.AddressView;
import com.hudfs.hudfs28.dtos.RestaurantProfileData;
import com.hudfs.hudfs28.dtos.RestaurantProfileResponse;
import com.hudfs.hudfs28.dtos.RestaurantRequest;
import com.hudfs.hudfs28.dtos.RestaurantResponse;
import com.hudfs.hudfs28.dtos.RestaurantUpdateRequest;
import com.hudfs.hudfs28.entities.Address;
import com.hudfs.hudfs28.entities.Restaurant;
import com.hudfs.hudfs28.entities.RestaurantRating;
import com.hudfs.hudfs28.repositories.AddressRepository;
import com.hudfs.hudfs28.repositories.RestaurantRepository;
import com.hudfs.hudfs28.repositories.RestaurantRatingRepository;

import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RestaurantRatingRepository restaurantRatingRepository;

    public RestaurantResponse register(RestaurantRequest request) {
        if (isNullOrEmpty(request.getName()) ||
            isNullOrEmpty(request.getOwner()) ||
            isNullOrEmpty(request.getMail()) ||
            isNullOrEmpty(request.getPassword()) ||
            isNullOrEmpty(request.getPasswordVerification())) {
            return new RestaurantResponse(false, null, null, "All the parts must be filled.");
        }

        if (!request.getPassword().equals(request.getPasswordVerification())) {
            return new RestaurantResponse(false, null, null, "Password and password verification do not match.");
        }

        if (!request.getMail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new RestaurantResponse(false, null, null, "Mail format is not correct");
        }

        if (!request.getPassword().matches("^(?=.*[A-Z])(?=.*\\p{Punct}).{8,}$")) {
            return new RestaurantResponse(false, null, null, "Password format is not correct.");
        }

        if (restaurantRepository.existsByMail(request.getMail())) {
            return new RestaurantResponse(false, null, null, "Mail is already registered.");
        }

        Restaurant restaurant = new Restaurant(
            request.getName(),
            request.getOwner(),
            request.getMail(),
            request.getPassword()
        );

        restaurantRepository.save(restaurant);

        // 👇 Generate JWT token after registration
        String token = JwtUtil.generateToken(restaurant.getMail());

        return new RestaurantResponse(true, token, restaurant.getId(), "Restaurant is registered.");
    }

    public RestaurantProfileData getProfile(String token, Long restaurantId) {
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return null;
        }

        AddressView addressView = null;
        Address address = restaurant.getAddress();
        if (address != null && address.getAddressId() != null) {
            addressView = new AddressView();
            addressView.setAddressId(address.getAddressId());
            addressView.setCountry(address.getCountry());
            addressView.setCity(address.getCity());
            addressView.setState(address.getState());
            addressView.setStreet(address.getStreet());
            addressView.setApartmentNumber(address.getApartmentNumber());
            addressView.setFloor(address.getFloor());
            addressView.setFlatNumber(address.getFlatNumber());
            addressView.setPostalCode(address.getPostalCode());
        }

        return new RestaurantProfileData(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getOwner(),
                restaurant.getMail(),
                restaurant.getPhoneNumber(),
                restaurant.getOverAllRating(),
                addressView,
                restaurant.isOpen()
        );
    }


    public RestaurantProfileResponse updateProfile(String token, Long restaurantId, RestaurantUpdateRequest request) {
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return new RestaurantProfileResponse(false, "Unauthorized.");
        }

        boolean changed = false;

        if (!isNullOrEmpty(request.getName()) && !request.getName().equals(restaurant.getName())) {
            restaurant.setName(request.getName());
            changed = true;
        }

        if (!isNullOrEmpty(request.getPhoneNumber()) && !request.getPhoneNumber().equals(restaurant.getPhoneNumber())) {
            restaurant.setPhoneNumber(request.getPhoneNumber());
            changed = true;
        }

        if (!changed) {
            return new RestaurantProfileResponse(false, "There is no change.");
        }

        restaurantRepository.save(restaurant);
        return new RestaurantProfileResponse(true, "Changes updated.");
    }

    public RestaurantProfileResponse addAddress(String token, Long restaurantId, AddressRequest request) {
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return new RestaurantProfileResponse(false, "Unauthorized.");
        }

        if (restaurant.getAddress() != null) {
            return new RestaurantProfileResponse(false, "Address already exists. Use update instead.");
        }

        Address address = new Address();
        address.setCountry(request.getCountry());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setStreet(request.getStreet());
        address.setApartmentNumber(request.getApartmentNumber());
        address.setFloor(request.getFloor());
        address.setFlatNumber(request.getFlatNumber());
        address.setPostalCode(request.getPostalCode());
        address.setRestaurant(restaurant);

        restaurant.setAddress(address);
        restaurantRepository.save(restaurant);

        return new RestaurantProfileResponse(true, "Address added successfully.");
    }

    public RestaurantProfileResponse updateAddress(String token, Long restaurantId, Long addressId, AddressRequest request) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(email);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return new RestaurantProfileResponse(false, "Unauthorized.");
        }

        Address addressToUpdate = restaurant.getAddress();

        if (addressToUpdate == null || !addressToUpdate.getAddressId().equals(addressId)) {
            return new RestaurantProfileResponse(false, "Address not found.");
        }

        if (isNullOrEmpty(request.getCountry()) &&
            isNullOrEmpty(request.getCity()) &&
            isNullOrEmpty(request.getState()) &&
            isNullOrEmpty(request.getStreet()) &&
            isNullOrEmpty(request.getApartmentNumber()) &&
            isNullOrEmpty(request.getFloor()) &&
            isNullOrEmpty(request.getFlatNumber()) &&
            isNullOrEmpty(request.getPostalCode())) {
            return new RestaurantProfileResponse(false, "At least one field must be filled.");
        }

        boolean changed = false;

        if (!isNullOrEmpty(request.getCountry()) && !request.getCountry().equals(addressToUpdate.getCountry())) {
            addressToUpdate.setCountry(request.getCountry()); changed = true;
        }
        if (!isNullOrEmpty(request.getCity()) && !request.getCity().equals(addressToUpdate.getCity())) {
            addressToUpdate.setCity(request.getCity()); changed = true;
        }
        if (!isNullOrEmpty(request.getState()) && !request.getState().equals(addressToUpdate.getState())) {
            addressToUpdate.setState(request.getState()); changed = true;
        }
        if (!isNullOrEmpty(request.getStreet()) && !request.getStreet().equals(addressToUpdate.getStreet())) {
            addressToUpdate.setStreet(request.getStreet()); changed = true;
        }
        if (!isNullOrEmpty(request.getApartmentNumber()) && !request.getApartmentNumber().equals(addressToUpdate.getApartmentNumber())) {
            addressToUpdate.setApartmentNumber(request.getApartmentNumber()); changed = true;
        }
        if (!isNullOrEmpty(request.getFloor()) && !request.getFloor().equals(addressToUpdate.getFloor())) {
            addressToUpdate.setFloor(request.getFloor()); changed = true;
        }
        if (!isNullOrEmpty(request.getFlatNumber()) && !request.getFlatNumber().equals(addressToUpdate.getFlatNumber())) {
            addressToUpdate.setFlatNumber(request.getFlatNumber()); changed = true;
        }
        if (!isNullOrEmpty(request.getPostalCode()) && !request.getPostalCode().equals(addressToUpdate.getPostalCode())) {
            addressToUpdate.setPostalCode(request.getPostalCode()); changed = true;
        }

        if (!changed) {
            return new RestaurantProfileResponse(false, "There is no change.");
        }

        addressRepository.save(addressToUpdate);
        return new RestaurantProfileResponse(true, "Address updated successfully.");
    }

    public RestaurantProfileResponse deleteAddress(String token, Long restaurantId, Long addressId) {
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return new RestaurantProfileResponse(false, "Unauthorized.");
        }

        Address address = restaurant.getAddress();

        if (address == null || !address.getAddressId().equals(addressId)) {
            return new RestaurantProfileResponse(false, "Address not found or does not belong to restaurant.");
        }

        restaurant.setAddress(null);
        addressRepository.delete(address);

        return new RestaurantProfileResponse(true, "Address deleted successfully.");
    }

    public RestaurantProfileResponse toggleRestaurantStatus(String token, Long restaurantId) {
        String mail = JwtUtil.validateTokenAndGetEmail(token);
        Restaurant restaurant = restaurantRepository.findByMail(mail);

        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return new RestaurantProfileResponse(false, "Unauthorized.");
        }

        restaurant.setOpen(!restaurant.isOpen());
        restaurantRepository.save(restaurant);

        return new RestaurantProfileResponse(true,
                restaurant.isOpen() ? "Restaurant is now OPEN." : "Restaurant is now CLOSED.");
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public ResponseEntity<?> getRestaurantRatings(Long restaurantId) {
        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("success", false, "detail", "Restaurant not found"));
        }

        Restaurant restaurant = restaurantOpt.get();
        List<RestaurantRating> ratings = restaurantRatingRepository.findByRestaurant(restaurant);
        List<Map<String, Object>> reviews = ratings.stream().map(rating -> {
            Map<String, Object> map = new HashMap<>();
            map.put("review-ratingId", rating.getId());
            map.put("rating", rating.getRating());
            map.put("review", rating.getReview());
            map.put("createdAt", rating.getCreatedAt());
            map.put("customerName", rating.getCustomer().getName());
            return map;
        }).toList();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "restaurantName", restaurant.getName(),
                "overallRating", restaurant.getOverAllRating(),
                "reviews", reviews
        ));
    }

}
