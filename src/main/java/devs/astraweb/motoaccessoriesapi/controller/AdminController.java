package devs.astraweb.motoaccessoriesapi.controller;

import devs.astraweb.motoaccessoriesapi.Dto.OrderResponse;
import devs.astraweb.motoaccessoriesapi.Dto.UserResponse;
import devs.astraweb.motoaccessoriesapi.model.Order;
import devs.astraweb.motoaccessoriesapi.repository.UserRepository;
import devs.astraweb.motoaccessoriesapi.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public AdminController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id,
                                                           @RequestBody UpdateStatusRequest request) {
        Order.Status status = Order.Status.valueOf(request.status().toUpperCase());
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    public record UpdateStatusRequest(String status) {}
}