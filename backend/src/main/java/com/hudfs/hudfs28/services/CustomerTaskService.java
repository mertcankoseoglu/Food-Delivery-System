package com.hudfs.hudfs28.services;

import com.hudfs.hudfs28.dtos.CustomerTaskRequest;
import com.hudfs.hudfs28.dtos.CustomerTaskResponse;
import com.hudfs.hudfs28.dtos.CustomerTaskView;
import com.hudfs.hudfs28.entities.Customer;
import com.hudfs.hudfs28.entities.CustomerTask;
import com.hudfs.hudfs28.entities.CustomerTask.TaskType;
import com.hudfs.hudfs28.entities.Menu;
import com.hudfs.hudfs28.entities.Product;
import com.hudfs.hudfs28.repositories.CustomerRepository;
import com.hudfs.hudfs28.repositories.CustomerTaskRepository;
import com.hudfs.hudfs28.repositories.MenuRepository;
import com.hudfs.hudfs28.repositories.ProductRepository;
import com.hudfs.hudfs28.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerTaskService {
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private CustomerTaskRepository customerTaskRepository;

    public CustomerTaskResponse assignTaskToCustomer(String token, Long customerId, CustomerTaskRequest request) {
        String adminUsername = JwtUtil.validateTokenAndGetEmail(token);
        if (!adminUsername.equals("admin")) {
            return new CustomerTaskResponse(false, "Unauthorized: only admin can assign tasks.");
        }
    
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return new CustomerTaskResponse(false, "Customer not found.");
        }
        
        if (request.getRequiredQuantity() == null ||
            request.getRewardPoints() == null ||
            request.getDeadline() == null ||
            (request.getProductId() == null && request.getMenuId() == null)) {
            return new CustomerTaskResponse(false, "All parts must be filled. (Only one of the product or menu)");
        }
        
        Customer customer = customerOpt.get();
        CustomerTask task = new CustomerTask();
        task.setCustomer(customer);
        task.setRequiredQuantity(request.getRequiredQuantity());
        task.setRewardPoints(request.getRewardPoints());
        task.setDeadline(request.getDeadline());
    
        if (request.getProductId() != null && request.getMenuId() == null) {
            Optional<Product> productOpt = productRepository.findById(request.getProductId());
            if (productOpt.isEmpty()) {
                return new CustomerTaskResponse(false, "Product not found.");
            }
            task.setProduct(productOpt.get());
            task.setTaskType(TaskType.PRODUCT);
        } else if (request.getMenuId() != null && request.getProductId() == null) {
            Optional<Menu> menuOpt = menuRepository.findById(request.getMenuId());
            if (menuOpt.isEmpty()) {
                return new CustomerTaskResponse(false, "Menu not found.");
            }
            task.setMenu(menuOpt.get());
            task.setTaskType(TaskType.MENU);
        } else {
            return new CustomerTaskResponse(false, "Either productId or menuId must be set, not both.");
        }
    
        task.setProgress(0);
        task.setCompleted(false);
        customerTaskRepository.save(task);
        return new CustomerTaskResponse(true, "Task assigned to customer.");
    }
    
    public List<CustomerTaskView> getTasksForCustomer(String token, Long customerId) {
        String adminUsername = JwtUtil.validateTokenAndGetEmail(token);
        if (!adminUsername.equals("admin")) { return List.of(); // unauthorized
        }

        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return List.of(); // customer not found
        }

        Customer customer = customerOpt.get();

        // Delete the deadline expired tasks
        List<CustomerTask> expiredTasks = customerTaskRepository.findByCustomerAndDeadlineBefore(customer, LocalDateTime.now());
        if (!expiredTasks.isEmpty()) {
            customerTaskRepository.deleteAll(expiredTasks);
        }

        // Get not expired tasks
        List<CustomerTask> activeTasks = customerTaskRepository.findByCustomerAndDeadlineAfter(customer, LocalDateTime.now());

        return activeTasks.stream().map(task -> {
            String taskType = task.getTaskType() != null ? task.getTaskType().toString() : "UNKNOWN";
            String targetName = task.getTaskType() == TaskType.PRODUCT
                    ? (task.getProduct() != null ? task.getProduct().getName() : "N/A")
                    : (task.getMenu() != null ? task.getMenu().getName() : "N/A");
            return new CustomerTaskView(
                    task.getCustomerTaskId(),
                    taskType,
                    targetName,
                    task.getRequiredQuantity(),
                    task.getProgress(),
                    task.getCompleted(),
                    task.getRewardPoints(),
                    task.getDeadline()
            );
        }).collect(Collectors.toList());
    }

    public List<CustomerTaskView> getTasksForLoggedInCustomer(String token) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepository.findByMail(email);
        if (customer == null) {
            return List.of(); // empty list if customer not found
        }
    
        // Delete the deadline expired tasks
        List<CustomerTask> expiredTasks = customerTaskRepository.findByCustomerAndDeadlineBefore(customer, LocalDateTime.now());
        customerTaskRepository.deleteAll(expiredTasks);

        // Get not expired tasks
        List<CustomerTask> activeTasks = customerTaskRepository.findByCustomerAndDeadlineAfter(customer, LocalDateTime.now());

        return activeTasks.stream()
            .map(task -> {
                String itemName;
                if (task.getTaskType() == CustomerTask.TaskType.PRODUCT && task.getProduct() != null) {
                    itemName = task.getProduct().getName();
                } else if (task.getTaskType() == CustomerTask.TaskType.MENU && task.getMenu() != null) {
                    itemName = task.getMenu().getName();
                } else {
                    itemName = "Unknown";
                }

                return new CustomerTaskView(
                        task.getCustomerTaskId(),
                        task.getTaskType() != null ? task.getTaskType().name() : "UNKNOWN",
                        itemName,
                        task.getRequiredQuantity(),
                        task.getProgress(),
                        task.getCompleted(),
                        task.getRewardPoints(),
                        task.getDeadline()
                );
            })
            .collect(Collectors.toList());
    }
    
    public CustomerTaskResponse selectCustomerTask(String token, Long customerId, Long customerTaskId) {
        try {
            String email = JwtUtil.validateTokenAndGetEmail(token);
            Customer customer = customerRepository.findByMail(email);
    
            if (customer == null || !customer.getCustomerId().equals(customerId)) {
                return new CustomerTaskResponse(false, "Unauthorized.");
            }
    
            Optional<CustomerTask> taskOpt = customerTaskRepository.findById(customerTaskId);
            if (taskOpt.isEmpty() || !taskOpt.get().getCustomer().getCustomerId().equals(customerId)) {
                return new CustomerTaskResponse(false, "Task cannot be found");
            }
    
            CustomerTask task = taskOpt.get();
            if (Boolean.TRUE.equals(task.getAssigned())) {
                return new CustomerTaskResponse(false, "Task is already assigned.");
            }
    
            task.setAssigned(true);
            customerTaskRepository.save(task);
    
            return new CustomerTaskResponse(true, "Task is assigned.");
        } catch (Exception ex) {
            ex.printStackTrace(); // Log this for debugging
            return new CustomerTaskResponse(false, "An unexpected error occurred.");
        }
    }
    
    
    public List<CustomerTaskView> getAssignedTasksForCustomer(String token, Long customerId) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepository.findByMail(email);
    
        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return List.of(); // unauthorized
        }
    
        List<CustomerTask> assignedTasks = customerTaskRepository.findByCustomerAndAssignedTrue(customer);
    
        return assignedTasks.stream()
            .map(task -> {
                String name = (task.getTaskType() == TaskType.PRODUCT && task.getProduct() != null)
                        ? task.getProduct().getName()
                        : (task.getTaskType() == TaskType.MENU && task.getMenu() != null)
                            ? task.getMenu().getName()
                            : "Unknown";
    
                return new CustomerTaskView(
                    task.getCustomerTaskId(),
                    task.getTaskType() != null ? task.getTaskType().name() : "UNKNOWN",
                    name,
                    task.getRequiredQuantity(),
                    task.getProgress(),
                    task.getCompleted(),
                    task.getRewardPoints(),
                    task.getDeadline()
                );
            })
            .collect(Collectors.toList());
    }

    public CustomerTaskResponse unassignCustomerTask(String token, Long customerId, Long customerTaskId) {
        String email = JwtUtil.validateTokenAndGetEmail(token);
        Customer customer = customerRepository.findByMail(email);
    
        if (customer == null || !customer.getCustomerId().equals(customerId)) {
            return new CustomerTaskResponse(false, "Unauthorized.");
        }
    
        Optional<CustomerTask> taskOpt = customerTaskRepository.findById(customerTaskId);
        if (taskOpt.isEmpty() || !taskOpt.get().getCustomer().getCustomerId().equals(customerId)) {
            return new CustomerTaskResponse(false, "Task cannot be found.");
        }
    
        CustomerTask task = taskOpt.get();
        if (!Boolean.TRUE.equals(task.getAssigned())) {
            return new CustomerTaskResponse(false, "Task is not currently assigned.");
        }
    
        task.setAssigned(false);
        customerTaskRepository.save(task);
    
        return new CustomerTaskResponse(true, "Task unassigned successfully.");
    }    
    
}
