package devs.astraweb.motoaccessoriesapi.service;

import devs.astraweb.motoaccessoriesapi.Dto.WishlistItemRequest;
import devs.astraweb.motoaccessoriesapi.Dto.WishlistItemResponse;
import devs.astraweb.motoaccessoriesapi.model.Product;
import devs.astraweb.motoaccessoriesapi.model.User;
import devs.astraweb.motoaccessoriesapi.model.WishlistItem;
import devs.astraweb.motoaccessoriesapi.repository.ProductRepository;
import devs.astraweb.motoaccessoriesapi.repository.UserRepository;
import devs.astraweb.motoaccessoriesapi.repository.WishlistItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishlistService(WishlistItemRepository wishlistItemRepository,
                            ProductRepository productRepository,
                            UserRepository userRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<WishlistItemResponse> getWishlist(String userEmail) {
        User user = findUserByEmail(userEmail);
        return wishlistItemRepository.findByUserId(user.getId()).stream()
                .map(WishlistItemResponse::new)
                .collect(Collectors.toList());
    }

    public WishlistItemResponse addItem(String userEmail, WishlistItemRequest request) {
        User user = findUserByEmail(userEmail);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + request.getProductId()));

        // Adding a product already in the wishlist is a no-op that just returns the existing entry
        WishlistItem existing = wishlistItemRepository.findByUserIdAndProductId(user.getId(), product.getId())
                .orElse(null);
        if (existing != null) {
            return new WishlistItemResponse(existing);
        }

        WishlistItem saved = wishlistItemRepository.save(new WishlistItem(user, product));
        return new WishlistItemResponse(saved);
    }

    public void removeItem(String userEmail, Long wishlistItemId) {
        User user = findUserByEmail(userEmail);
        WishlistItem item = wishlistItemRepository.findById(wishlistItemId)
                .orElseThrow(() -> new IllegalArgumentException("Wishlist item not found with id: " + wishlistItemId));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Wishlist item not found with id: " + wishlistItemId);
        }

        wishlistItemRepository.delete(item);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }
}
