package devs.astraweb.motoaccessoriesapi.controller;

import devs.astraweb.motoaccessoriesapi.Dto.CartItemRequest;
import devs.astraweb.motoaccessoriesapi.Dto.UpdateQuantityRequest;
import devs.astraweb.motoaccessoriesapi.Dto.CartItemResponse;
import devs.astraweb.motoaccessoriesapi.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(Authentication authentication) {
        return ResponseEntity.ok(cartService.getCart(authentication.getName()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addItem(Authentication authentication,
                                                       @Valid @RequestBody CartItemRequest request) {
        CartItemResponse response = cartService.addItem(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<CartItemResponse> updateQuantity(Authentication authentication,
                                                              @PathVariable Long id,
                                                              @Valid @RequestBody UpdateQuantityRequest request) {
        return ResponseEntity.ok(cartService.updateQuantity(authentication.getName(), id, request));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(Authentication authentication, @PathVariable Long id) {
        cartService.removeItem(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
