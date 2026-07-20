package devs.astraweb.motoaccessoriesapi.controller;

import devs.astraweb.motoaccessoriesapi.Dto.WishlistItemRequest;
import devs.astraweb.motoaccessoriesapi.Dto.WishlistItemResponse;
import devs.astraweb.motoaccessoriesapi.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<List<WishlistItemResponse>> getWishlist(Authentication authentication) {
        return ResponseEntity.ok(wishlistService.getWishlist(authentication.getName()));
    }

    @PostMapping("/items")
    public ResponseEntity<WishlistItemResponse> addItem(Authentication authentication,
                                                           @Valid @RequestBody WishlistItemRequest request) {
        WishlistItemResponse response = wishlistService.addItem(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(Authentication authentication, @PathVariable Long id) {
        wishlistService.removeItem(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
