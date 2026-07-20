package devs.astraweb.motoaccessoriesapi.repository;

import devs.astraweb.motoaccessoriesapi.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(Long userId);

    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

    // Used after checkout to wipe the user's cart in one call instead of deleting item by item
    void deleteByUserId(Long userId);
}
