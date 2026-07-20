package devs.astraweb.motoaccessoriesapi.service;

import devs.astraweb.motoaccessoriesapi.Dto.OrderResponse;
import devs.astraweb.motoaccessoriesapi.model.*;
import devs.astraweb.motoaccessoriesapi.repository.CartItemRepository;
import devs.astraweb.motoaccessoriesapi.repository.OrderRepository;
import devs.astraweb.motoaccessoriesapi.repository.ProductRepository;
import devs.astraweb.motoaccessoriesapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                         CartItemRepository cartItemRepository,
                         ProductRepository productRepository,
                         UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // @Transactional ensures stock decrements, order creation, and cart clearing either
    // all succeed together or all roll back together - no partial checkout state.
    @Transactional
    public OrderResponse checkout(String userEmail) {
        User user = findUserByEmail(userEmail);
        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cannot checkout with an empty cart");
        }

        // Validate stock for every item before touching anything, so we fail fast without
        // partially decrementing stock for earlier items in the loop
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for " + product.getName() +
                                " (requested " + item.getQuantity() + ", available " + product.getStockQuantity() + ")");
            }
        }

        Order order = new Order();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            Product product = item.getProduct();

            OrderItem orderItem = new OrderItem(product, item.getQuantity(), product.getPrice());
            order.addItem(orderItem);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(total);
        order.setStatus(Order.Status.PENDING);

        Order saved = orderRepository.save(order);
        cartItemRepository.deleteByUserId(user.getId());

        return new OrderResponse(saved);
    }

    public List<OrderResponse> getOrderHistory(String userEmail) {
        User user = findUserByEmail(userEmail);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(String userEmail, Long orderId) {
        User user = findUserByEmail(userEmail);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }

        return new OrderResponse(order);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }
}
