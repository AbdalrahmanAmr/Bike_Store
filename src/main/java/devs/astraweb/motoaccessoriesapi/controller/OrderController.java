package devs.astraweb.motoaccessoriesapi.controller;

import devs.astraweb.motoaccessoriesapi.Dto.CheckoutRequest;
import devs.astraweb.motoaccessoriesapi.Dto.OrderResponse;
import devs.astraweb.motoaccessoriesapi.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> checkout(Authentication authentication,
                                                  @Valid @RequestBody CheckoutRequest request) {
        OrderResponse response = orderService.checkout(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrderHistory(Authentication authentication) {
        return ResponseEntity.ok(orderService.getOrderHistory(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(authentication.getName(), id));
    }
}