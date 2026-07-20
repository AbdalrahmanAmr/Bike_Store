package devs.astraweb.motoaccessoriesapi.service;

import devs.astraweb.motoaccessoriesapi.Dto.CartItemRequest;
import devs.astraweb.motoaccessoriesapi.Dto.UpdateQuantityRequest;
import devs.astraweb.motoaccessoriesapi.Dto.CartItemResponse;
import devs.astraweb.motoaccessoriesapi.model.CartItem;
import devs.astraweb.motoaccessoriesapi.model.Product;
import devs.astraweb.motoaccessoriesapi.model.User;
import devs.astraweb.motoaccessoriesapi.repository.CartItemRepository;
import devs.astraweb.motoaccessoriesapi.repository.ProductRepository;
import devs.astraweb.motoaccessoriesapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartItemRepository cartItemRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<CartItemResponse> getCart(String userEmail) {
        User user = findUserByEmail(userEmail);
        return cartItemRepository.findByUserId(user.getId()).stream()
                .map(CartItemResponse::new)
                .collect(Collectors.toList());
    }

    public CartItemResponse addItem(String userEmail, CartItemRequest request) {
        User user = findUserByEmail(userEmail);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + request.getProductId()));

        // If this product is already in the cart, bump the quantity instead of creating a duplicate row
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId())
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = new CartItem(user, product, request.getQuantity());
        }

        CartItem saved = cartItemRepository.save(cartItem);
        return new CartItemResponse(saved);
    }

    public CartItemResponse updateQuantity(String userEmail, Long cartItemId, UpdateQuantityRequest request) {
        CartItem cartItem = findOwnedCartItem(userEmail, cartItemId);
        cartItem.setQuantity(request.getQuantity());
        CartItem saved = cartItemRepository.save(cartItem);
        return new CartItemResponse(saved);
    }

    public void removeItem(String userEmail, Long cartItemId) {
        CartItem cartItem = findOwnedCartItem(userEmail, cartItemId);
        cartItemRepository.delete(cartItem);
    }

    private CartItem findOwnedCartItem(String userEmail, Long cartItemId) {
        User user = findUserByEmail(userEmail);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + cartItemId));

        // Ownership check - a user must never be able to modify another user's cart item by guessing an id
        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Cart item not found with id: " + cartItemId);
        }

        return cartItem;
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }
}
