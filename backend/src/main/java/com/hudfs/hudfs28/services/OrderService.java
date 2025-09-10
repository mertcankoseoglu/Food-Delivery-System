package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.AddressView;
import com.hudfs.hudfs28.dtos.OrderListItem;
import com.hudfs.hudfs28.dtos.OrderPlacementRequest;
import com.hudfs.hudfs28.dtos.OrderStatusUpdateRequest;
import com.hudfs.hudfs28.entities.*;
import com.hudfs.hudfs28.repositories.*;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired private CustomerRepository customerRepo;
    @Autowired private RestaurantRepository restaurantRepo;
    @Autowired private AddressRepository addressRepo;
    @Autowired private CreditCardRepository creditCardRepo;
    @Autowired private CartRepository cartRepo;
    @Autowired private CartItemRepository cartItemRepo;
    @Autowired private CourierRepository courierRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private OrderItemRepository orderItemRepo;
    @Autowired private CustomerTaskRepository taskRepo;
    @Autowired private CouponRepository couponRepo;
    @Autowired private CustomerService customerService;

    public Map<String, Object> placeOrder(Long customerId, OrderPlacementRequest request, String token) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        Customer customer = customerRepo.findByMail(email);

        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return fail("Unauthorized.");
        }

        Optional<Address> addressOpt = addressRepo.findById(request.getAddressId());
        Optional<CreditCard> cardOpt = creditCardRepo.findById(request.getCreditCardId());

        if (addressOpt.isEmpty() || !addressOpt.get().getCustomer().equals(customer)) {
            return fail("Invalid address.");
        }

        if (cardOpt.isEmpty() || !cardOpt.get().getCustomer().equals(customer)) {
            return fail("Invalid credit card.");
        }

        Cart cart = cartRepo.findByCustomer(customer);
        if (cart == null || cart.getItems().isEmpty()) return fail("Cart is empty.");

        for (CartItem item : cart.getItems()) {
            if (item.isProductItem()) {
                if (!item.getProduct().isInStock() || !item.getProduct().getRestaurant().isOpen()) {
                    return fail("Product not available or restaurant closed.");
                }
            } else if (item.isMenuItem()) {
                if (!item.getMenu().isInStock() || !item.getMenu().getRestaurant().isOpen()) {
                    return fail("Menu not available or restaurant closed.");
                }
            }
        }

        Courier courier = courierRepo.findFirstByAvailableTrue().orElse(null);
        if (courier == null) return fail("No courier available.");
        courier.setAvailable(false);

        // === Fiyat hesaplama ===
        float totalBeforeDiscount = 0f;
        for (CartItem item : cart.getItems()) {
            float unitPrice = item.isProductItem()
                    ? item.getProduct().getPrice()
                    : item.getMenu().getPrice();
            totalBeforeDiscount += unitPrice * item.getQuantity();
        }

        float discount = 0f;
        String couponCode = null;
        Coupon coupon = null;


        if (request.getCouponId() != null) {
            coupon = couponRepo.findById(request.getCouponId()).orElse(null);

            if (coupon != null &&
                coupon.getCustomer().equals(customer) &&
                coupon.getExpiryDate().after(new Date()) &&
                totalBeforeDiscount >= coupon.getMinimumOrderAmount()) {

                discount = coupon.getDiscountAmount();
                couponCode = coupon.getCode();
                couponRepo.delete(coupon);
            }
        }

        float finalPrice = Math.max(0f, totalBeforeDiscount - discount);

        // === Siparişi kaydet ===
        Order order = new Order();
        order.setCustomer(customer);
        order.setAddress(addressOpt.get());
        order.setCreditCard(cardOpt.get());
        order.setCourier(courier);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(totalBeforeDiscount);
        order.setFinalPrice(finalPrice);
        orderRepo.save(order);

        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(item.getQuantity());

            if (item.isProductItem()) {
                Product product = item.getProduct();
                orderItem.setProduct(product);
                orderItem.setPriceAtPurchase(product.getPrice());
                handleTask(customer, product, null, item.getQuantity());
            } else if (item.isMenuItem()) {
                Menu menu = item.getMenu();
                orderItem.setMenu(menu);
                orderItem.setPriceAtPurchase(menu.getPrice());
                handleTask(customer, null, menu, item.getQuantity());
            }

            orderItemRepo.save(orderItem);
        }

        customer.setPoints(customer.getPoints() + 10);
        customerRepo.save(customer);

        customerService.viewLevelAndPoints(customer.getCustomerId(), "Bearer " + token);

        cartItemRepo.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepo.save(cart);

        // === Response ===
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("detail", "Order placed successfully" + (couponCode != null ? " (Coupon applied)" : "") + ".");
        response.put("totalBeforeDiscount", totalBeforeDiscount);
        response.put("discountAmount", discount);
        response.put("finalPrice", finalPrice);
        if (couponCode != null) response.put("couponCode", couponCode);
        response.put("orderId", order.getId());

        return response;
    }

    private void handleTask(Customer customer, Product product, Menu menu, int qty) {
        List<CustomerTask> tasks = taskRepo.findByCustomerAndAssignedTrue(customer).stream()
            .filter(task -> !task.getCompleted())
            .toList();
        for (CustomerTask task : tasks) {
            if (task.getTaskType() == CustomerTask.TaskType.PRODUCT && product != null && task.getProduct().equals(product)) {
                task.setProgress(task.getProgress() + qty);
            } else if (task.getTaskType() == CustomerTask.TaskType.MENU && menu != null && task.getMenu().equals(menu)) {
                task.setProgress(task.getProgress() + qty);
            }

            if (task.getProgress() >= task.getRequiredQuantity() && !task.getCompleted()) {
                task.setCompleted(true);
                customer.setPoints(customer.getPoints() + task.getRewardPoints());
            }
        }

        taskRepo.saveAll(tasks);
    }

    public List<OrderListItem> getCustomerOrders(Customer customer) {
        return mapToListItems(orderRepo.findByCustomer(customer));
    }

    public List<OrderListItem> getCourierOrders(Courier courier) {
        return mapToListItems(orderRepo.findByCourier(courier));
    }

    public List<OrderListItem> getAllOrdersForAdmin() {
        return mapToListItems(orderRepo.findAll());
    }

    public List<OrderListItem> getRestaurantOrders(Restaurant restaurant) {
        List<Order> allOrders = orderRepo.findAll();
        List<Order> result = new ArrayList<>();

        for (Order order : allOrders) {
            boolean includesThisRestaurant = order.getItems().stream().anyMatch(item ->
                (item.getProduct() != null && item.getProduct().getRestaurant().equals(restaurant)) ||
                (item.getMenu() != null && item.getMenu().getRestaurant().equals(restaurant))
            );
            if (includesThisRestaurant) {
                result.add(order);
            }
        }

        return mapToListItems(result);
    }

    private List<OrderListItem> mapToListItems(List<Order> orders) {
        List<OrderListItem> list = new ArrayList<>();
    
        for (Order order : orders) {
            List<String> itemNames = new ArrayList<>();
            int itemCount = 0;
            Long restaurantId = null;
            String restaurantName = null;
    
            for (OrderItem item : order.getItems()) {
                itemCount += item.getQuantity();
    
                if (item.getProduct() != null && item.getProduct().getRestaurant() != null) {
                    if (restaurantId == null) {
                        restaurantId = item.getProduct().getRestaurant().getId();
                        restaurantName = item.getProduct().getRestaurant().getName();
                    }
                    itemNames.add(item.getProduct().getName());
                } else if (item.getMenu() != null && item.getMenu().getRestaurant() != null) {
                    if (restaurantId == null) {
                        restaurantId = item.getMenu().getRestaurant().getId();
                        restaurantName = item.getMenu().getRestaurant().getName();
                    }
                    itemNames.add(item.getMenu().getName());
                }
            }
    
            AddressView addressView = null;
            Address addr = order.getAddress();
            if (addr != null) {
                addressView = new AddressView();
                addressView.setAddressId(addr.getAddressId());
                addressView.setCountry(addr.getCountry());
                addressView.setCity(addr.getCity());
                addressView.setState(addr.getState());
                addressView.setStreet(addr.getStreet());
                addressView.setApartmentNumber(addr.getApartmentNumber());
                addressView.setFloor(addr.getFloor());
                addressView.setFlatNumber(addr.getFlatNumber());
                addressView.setPostalCode(addr.getPostalCode());
            }
    
            Courier courier = order.getCourier();
            String courierName = courier != null ? courier.getName() : null;
            String courierPhone = courier != null ? courier.getTelephoneNumber() : null;
            Integer courierId = courier != null ? courier.getCourierId() : null;
    
            boolean isCancelable = order.getStatus() == OrderStatus.PENDING;
            Long creditCardId = order.getCreditCard() != null ? order.getCreditCard().getCardId() : null;
    
            list.add(new OrderListItem(
                order.getId(),
                order.getStatus().name(),
                order.getCreatedAt(),
                itemCount,
                addressView,
                restaurantId,
                restaurantName,
                courierName,
                courierPhone,
                courierId,
                isCancelable,
                itemNames,
                creditCardId,
                order.getCustomer().getCustomerId(),
                order.getTotalPrice(),
                order.getFinalPrice()
            ));
        }
        return list;
    }

    private Map<String, Object> success(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("detail", msg);
        return map;
    }

    private Map<String, Object> fail(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("detail", msg);
        return map;
    }

    public Map<String, Object> updateStatusByRestaurant(Long restaurantId, Long orderId, OrderStatusUpdateRequest request, String token) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        Restaurant restaurant = restaurantRepo.findByMail(email);
        if (restaurant == null || !restaurant.getId().equals(restaurantId)) {
            return fail("Unauthorized.");
        }
    
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order == null || !orderBelongsToRestaurant(order, restaurant)) {
            return fail("Order not found or not associated with your restaurant.");
        }
    
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(request.getNewStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            return fail("Invalid status.");
        }
    
        if (newStatus != OrderStatus.PREPARING && newStatus != OrderStatus.READY) {
            return fail("Restaurant can only update to PREPARING or READY.");
        }
    
        order.setStatus(newStatus);
        orderRepo.save(order);
        return success("Order status updated to " + newStatus);
    }
    
    public Map<String, Object> updateStatusByCourier(Integer courierId, Long orderId, OrderStatusUpdateRequest request, String token) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        Courier courier = courierRepo.findByMail(email);
        if (courier == null || !courier.getCourierId().equals(courierId)) {
            return fail("Unauthorized.");
        }
    
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order == null || order.getCourier() == null || !order.getCourier().getCourierId().equals(courierId)) {
            return fail("Order not found or not assigned to you.");
        }
    
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(request.getNewStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            return fail("Invalid status.");
        }
    
        if (newStatus != OrderStatus.DELIVERING && newStatus != OrderStatus.DELIVERED) {
            return fail("Courier can only update to DELIVERING or DELIVERED.");
        }
    
        if (newStatus == OrderStatus.DELIVERED) {
            courier.setAvailable(true);
            courierRepo.save(courier);
        }
    
        order.setStatus(newStatus);
        orderRepo.save(order);
        return success("Order status updated to " + newStatus);
    }
    
    private boolean orderBelongsToRestaurant(Order order, Restaurant restaurant) {
        return order.getItems().stream().anyMatch(item ->
            (item.getProduct() != null && item.getProduct().getRestaurant().getId().equals(restaurant.getId())) ||
            (item.getMenu() != null && item.getMenu().getRestaurant().getId().equals(restaurant.getId()))
        );
    }    

    public Map<String, Object> cancelOrder(Long customerId, Long orderId, String token) {
        String email = JwtUtil.validateTokenAndGetEmail(token.replace("Bearer ", ""));
        Customer customer = customerRepo.findByMail(email);
    
        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return fail("Unauthorized.");
        }
    
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order == null || !order.getCustomer().getCustomerId().equals(customerId)) {
            return fail("Order not found or not yours.");
        }
    
        if (order.getStatus() != OrderStatus.PENDING) {
            return fail("You can only cancel a pending order.");
        }
    
        order.setStatus(OrderStatus.CANCELLED);
    
        // Release courier if assigned
        Courier courier = order.getCourier();
        if (courier != null) {
            courier.setAvailable(true);
            courierRepo.save(courier);
        }
    
        orderRepo.save(order);
    
        return success("Order has been cancelled.");
    }
    

}
