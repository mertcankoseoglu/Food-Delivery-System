package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.AddressRequest;
import com.hudfs.hudfs28.dtos.AddressView;
import com.hudfs.hudfs28.dtos.CreditCardRequest;
import com.hudfs.hudfs28.dtos.CreditCardView;
import com.hudfs.hudfs28.dtos.CustomerProfileUpdateRequest;
import com.hudfs.hudfs28.dtos.CustomerRequest;
import com.hudfs.hudfs28.dtos.CustomerResponse;
import com.hudfs.hudfs28.dtos.RestaurantRatingRequest;
import com.hudfs.hudfs28.dtos.CustomerProfileUpdateResponse;
import com.hudfs.hudfs28.dtos.CustomerProfileView;
import com.hudfs.hudfs28.entities.Customer;
import com.hudfs.hudfs28.entities.Restaurant;
import com.hudfs.hudfs28.entities.Address;
import com.hudfs.hudfs28.entities.Coupon;
import com.hudfs.hudfs28.entities.CreditCard;
import com.hudfs.hudfs28.entities.Order;
import com.hudfs.hudfs28.entities.RestaurantRating;
import com.hudfs.hudfs28.repositories.CouponRepository;
import com.hudfs.hudfs28.repositories.CustomerRepository;
import com.hudfs.hudfs28.repositories.RestaurantRepository;
import com.hudfs.hudfs28.repositories.RestaurantRatingRepository;
import com.hudfs.hudfs28.repositories.OrderRepository;

import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantRatingRepository restaurantRatingRepository;

    @Autowired
    private OrderRepository orderRepository;


    public CustomerResponse register(CustomerRequest request) {
        if (isNullOrEmpty(request.getName()) ||
            isNullOrEmpty(request.getMail()) ||
            isNullOrEmpty(request.getPassword()) ||
            isNullOrEmpty(request.getPasswordVerification())) {
            return new CustomerResponse(false, null, null, "All the parts must be filled.");
        }

        if (!request.getPassword().equals(request.getPasswordVerification())) {
            return new CustomerResponse(false, null, null, "Password and password verification do not match.");
        }

        if (!request.getMail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new CustomerResponse(false, null, null, "Mail format is not correct.");
        }

        if (!request.getPassword().matches("^(?=.*[A-Z])(?=.*\\p{Punct}).{8,}$")) {
            return new CustomerResponse(false, null, null, "Password format is not correct.");
        }

        if (customerRepository.existsByMail(request.getMail())) {
            return new CustomerResponse(false, null, null, "Mail is already registered.");
        }

        Customer customer = new Customer(
                request.getName(),
                request.getMail(),
                request.getPassword()
        );

        customerRepository.save(customer);

        String token = JwtUtil.generateToken(customer.getMail());

        return new CustomerResponse(true, token, customer.getCustomerId(), "customer is registered.");
    }

    public CustomerProfileUpdateResponse updateProfile(String token, Long customerId, CustomerProfileUpdateRequest request) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepository.findByMail(email);
    
        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return new CustomerProfileUpdateResponse(false, "Unauthorized.");
        }
        
        if (request.getPhoneNumber() != null &&
            !request.getPhoneNumber().matches("^0\\d{3} \\d{3} \\d{2} \\d{2}$")) {
            return new CustomerProfileUpdateResponse(false, "Phone number must be in format: 0XXX XXX XX XX.");
        }
        
        boolean changed = false;
        
        if (!isNullOrEmpty(request.getName()) && !request.getName().equals(customer.getName())) {
            customer.setName(request.getName());
            changed = true;
        }
        
        if (request.getAge() != null && !request.getAge().equals(customer.getAge())) {
            customer.setAge(request.getAge());
            changed = true;
        }
        
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(customer.getPhoneNumber())) {
            customer.setPhoneNumber(request.getPhoneNumber());
            changed = true;
        }
        
        if (!changed) {
            return new CustomerProfileUpdateResponse(false, "There is no change.");
        }
        
        customerRepository.save(customer);
        return new CustomerProfileUpdateResponse(true, "Profile updated successfully.");
    }
    
    public CustomerProfileUpdateResponse addAddress(String token, Long customerId, AddressRequest request) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepository.findByMail(email);
        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return new CustomerProfileUpdateResponse(false, "Unauthorized.");
        }
        if (isNullOrEmpty(request.getCountry()) ||
            isNullOrEmpty(request.getCity()) ||
            isNullOrEmpty(request.getState()) ||
            isNullOrEmpty(request.getStreet()) ||
            isNullOrEmpty(request.getApartmentNumber()) ||
            isNullOrEmpty(request.getFloor()) ||
            isNullOrEmpty(request.getFlatNumber()) ||
            isNullOrEmpty(request.getPostalCode())) {
            return new CustomerProfileUpdateResponse(false, "All the parts must be filled.");
        }

        Address address = new Address(
            request.getCountry(),
            request.getCity(),
            request.getState(),
            request.getStreet(),
            request.getApartmentNumber(),
            request.getFloor(),
            request.getFlatNumber(),
            request.getPostalCode()
        );
        address.setCustomer(customer);

        customer.getAddresses().add(address);
        customerRepository.save(customer);

        return new CustomerProfileUpdateResponse(true, "Address added successfully.");
    }

    public CustomerProfileUpdateResponse updateAddress(String token, Long customerId, Long addressId, AddressRequest request) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepository.findByMail(email);

        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return new CustomerProfileUpdateResponse(false, "Unauthorized.");
        }

        Address addressToUpdate = customer.getAddresses().stream()
            .filter(addr -> addr.getAddressId().equals(addressId))
            .findFirst()
            .orElse(null);

        if (addressToUpdate == null) {
            return new CustomerProfileUpdateResponse(false, "Address not found.");
        }

        if (isNullOrEmpty(request.getCountry()) &&
           isNullOrEmpty(request.getCity()) &&
           isNullOrEmpty(request.getState()) &&
           isNullOrEmpty(request.getStreet()) &&
           isNullOrEmpty(request.getApartmentNumber()) &&
           isNullOrEmpty(request.getFloor()) &&
           isNullOrEmpty(request.getFlatNumber()) &&
           isNullOrEmpty(request.getPostalCode())) {
            return new CustomerProfileUpdateResponse(false, "At least one field must be filled.");
        }

        boolean changed = false;

        if (!isNullOrEmpty(request.getCountry()) && !request.getCountry().equals(addressToUpdate.getCountry())) {
            addressToUpdate.setCountry(request.getCountry());
            changed = true;
        }
        if (!isNullOrEmpty(request.getCity()) && !request.getCity().equals(addressToUpdate.getCity())) {
            addressToUpdate.setCity(request.getCity());
            changed = true;
        }
        if (!isNullOrEmpty(request.getState()) && !request.getState().equals(addressToUpdate.getState())) {
            addressToUpdate.setState(request.getState());
            changed = true;
        }
        if (!isNullOrEmpty(request.getStreet()) && !request.getStreet().equals(addressToUpdate.getStreet())) {
            addressToUpdate.setStreet(request.getStreet());
            changed = true;
        }
        if (!isNullOrEmpty(request.getApartmentNumber()) && !request.getApartmentNumber().equals(addressToUpdate.getApartmentNumber())) {
            addressToUpdate.setApartmentNumber(request.getApartmentNumber());
            changed = true;
        }
        if (!isNullOrEmpty(request.getFloor()) && !request.getFloor().equals(addressToUpdate.getFloor())) {
            addressToUpdate.setFloor(request.getFloor());
            changed = true;
        }
        if (!isNullOrEmpty(request.getFlatNumber()) && !request.getFlatNumber().equals(addressToUpdate.getFlatNumber())) {
            addressToUpdate.setFlatNumber(request.getFlatNumber());
            changed = true;
        }
        if (!isNullOrEmpty(request.getPostalCode()) && !request.getPostalCode().equals(addressToUpdate.getPostalCode())) {
            addressToUpdate.setPostalCode(request.getPostalCode());
            changed = true;
        }

        if (!changed) {
            return new CustomerProfileUpdateResponse(false, "There is no change.");
        }

        customerRepository.save(customer);
        return new CustomerProfileUpdateResponse(true, "Address updated successfully.");
    }


    public CustomerProfileUpdateResponse deleteAddress(String token, Long customerId, Long addressId) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepository.findByMail(email);
    
        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return new CustomerProfileUpdateResponse(false, "Unauthorized.");
        }
    
        boolean removed = customer.getAddresses().removeIf(addr -> addr.getAddressId().equals(addressId));
        if (!removed) {
            return new CustomerProfileUpdateResponse(false, "Address not found or does not belong to customer.");
        }
    
        customerRepository.save(customer);
        return new CustomerProfileUpdateResponse(true, "Address deleted successfully.");
    }

    public CustomerProfileUpdateResponse addCreditCard(String token, Long customerId, CreditCardRequest request) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepository.findByMail(email);
        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return new CustomerProfileUpdateResponse(false, "Unauthorized.");
        }
        if (isNullOrEmpty(request.getCardNumber()) ||
            isNullOrEmpty(request.getExpiryDate()) ||
            isNullOrEmpty(request.getCvc())) {
            return new CustomerProfileUpdateResponse(false, "All the parts must be filled.");
        }

        if (!request.getCardNumber().matches("^[1-9][0-9]{15}$")) {
            return new CustomerProfileUpdateResponse(false, "Card number must be 16 digits and not start with 0.");
        }

        if (!request.getExpiryDate().matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            return new CustomerProfileUpdateResponse(false, "Expiry date must be in MM/YY format.");
        }

        if (!request.getCvc().matches("^\\d{3}$")) {
            return new CustomerProfileUpdateResponse(false, "CVC must be 3 digits.");
        }

        CreditCard card = new CreditCard(
            request.getCardNumber(),
            request.getExpiryDate(),
            request.getCvc()
        );
        card.setCustomer(customer);

        customer.getCreditCards().add(card);
        customerRepository.save(customer);

        return new CustomerProfileUpdateResponse(true, "Credit card added successfully.");
    }
    
    public CustomerProfileUpdateResponse deleteCreditCard(String token, Long customerId, Long cardId) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepository.findByMail(email);
    
        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return new CustomerProfileUpdateResponse(false, "Unauthorized.");
        }
    
        boolean removed = customer.getCreditCards().removeIf(card -> card.getCardId().equals(cardId));
        if (!removed) {
            return new CustomerProfileUpdateResponse(false, "Card not found or does not belong to customer.");
        }
    
        customerRepository.save(customer);
        return new CustomerProfileUpdateResponse(true, "Credit card deleted successfully.");
    }

    public CustomerProfileView viewProfile(String token, Long customerId) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepository.findByMail(email);
    
        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return null; // Or throw Unauthorized exception
        }
    
        CustomerProfileView view = new CustomerProfileView();
        view.setName(customer.getName());
        view.setMail(customer.getMail());
        view.setAge(customer.getAge());
        view.setPhoneNumber(customer.getPhoneNumber());
    
        List<AddressView> addressViews = customer.getAddresses().stream().map(addr -> {
            AddressView av = new AddressView();
            av.setAddressId(addr.getAddressId());
            av.setCountry(addr.getCountry());
            av.setCity(addr.getCity());
            av.setState(addr.getState());
            av.setStreet(addr.getStreet());
            av.setApartmentNumber(addr.getApartmentNumber());
            av.setFloor(addr.getFloor());
            av.setFlatNumber(addr.getFlatNumber());
            av.setPostalCode(addr.getPostalCode());
            return av;
        }).toList();
    
        List<CreditCardView> cardViews = customer.getCreditCards().stream().map(card -> {
            CreditCardView cv = new CreditCardView();
            cv.setCreditCardId(card.getCardId());
            cv.setCardNumber(card.getCardNumber());
            cv.setCardExpireDate(card.getCardExpireDate());
            cv.setCardCVC(card.getCardCVC());
            return cv;
        }).toList();
    
        view.setAddresses(addressViews);
        view.setCreditCards(cardViews);
        return view;
    }
    
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }


    public Map<String, Object> viewLevelAndPoints(Long customerId, String token) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        Customer customer = customerRepository.findByMail(email);

        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return Map.of("success", false, "detail", "unauthorized");
        }

        int points = customer.getPoints();
        int level = points / 100;

        if (level > customer.getLevel()) {
            customer.setLevel(level);
            customerRepository.save(customer); // persist new level

            float discount = switch (level) {
                case 1 -> 20f;
                case 2 -> 30f;
                case 3 -> 40f;
                default -> 50f;
            };

            float minOrderAmount = switch (level) {
                case 1 -> 100f;
                case 2 -> 150f;
                case 3 -> 200f;
                default -> 250f;
            };

            Coupon coupon = new Coupon();
            coupon.setCode("LEVEL" + level + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
            coupon.setDiscountAmount(discount);
            coupon.setMinimumOrderAmount(minOrderAmount);
            coupon.setExpiryDate(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000)); // 7 days
            coupon.setCustomer(customer);

            couponRepository.save(coupon);
        }

        return Map.of(
                "success", true,
                "level", customer.getLevel(),
                "points", customer.getPoints()
        );
    }

    public Map<String, Object> rateRestaurant(Long customerId, String token, RestaurantRatingRequest request) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        Customer customer = customerRepository.findByMail(email);

        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return Map.of("success", false, "detail", "Unauthorized");
        }

        Optional<Order> orderOpt = orderRepository.findById(request.getOrderId());
        if (orderOpt.isEmpty()) {
            return Map.of("success", false, "detail", "Order not found");
        }

        Order order = orderOpt.get();
        if (!order.getCustomer().getCustomerId().equals(customerId)) {
            return Map.of("success", false, "detail", "Order doesn't belong to customer");
        }

        if (order.getCreatedAt().plusDays(1).isBefore(LocalDateTime.now())) {
            return Map.of("success", false, "detail", "You can only rate within 1 day of order");
        }

        Optional<RestaurantRating> existing = restaurantRatingRepository.findByCustomerAndOrder(customer, order);
        if (existing.isPresent()) {
            return Map.of("success", false, "detail", "You already rated this order");
        }

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId()).orElse(null);
        if (restaurant == null) {
            return Map.of("success", false, "detail", "Restaurant not found");
        }

        RestaurantRating rating = new RestaurantRating();
        rating.setCustomer(customer);
        rating.setRestaurant(restaurant);
        rating.setOrder(order);
        rating.setCreatedAt(LocalDateTime.now());
        rating.setRating(request.getRating());
        rating.setReview(request.getReview());

        restaurantRatingRepository.save(rating);

        
        List<RestaurantRating> allRatings = restaurantRatingRepository.findByRestaurant(restaurant);
        float avg = (float) allRatings.stream().mapToInt(RestaurantRating::getRating).average().orElse(0);
        restaurant.setOverAllRating(avg);
        restaurantRepository.save(restaurant);

        return Map.of("success", true, "detail", "Rating submitted successfully");
    }

}

