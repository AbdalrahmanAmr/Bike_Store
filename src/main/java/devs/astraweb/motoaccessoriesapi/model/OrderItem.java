package devs.astraweb.motoaccessoriesapi.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Nullable on purpose: if the product is later deleted (e.g. discontinued after being
    // shipped), this FK is set to null via ON DELETE SET NULL, but the order history survives
    // using the productName/priceAtPurchase snapshots below.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true, foreignKey = @ForeignKey(name = "fk_order_items_product",
            foreignKeyDefinition = "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL"))
    private Product product;

    // Snapshot of the product name at purchase time - survives product deletion, unlike product.getName()
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    // Snapshot of the product price at purchase time - never read live product price for historical orders
    @Column(name = "price_at_purchase", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtPurchase;

    public OrderItem() {
    }

    public OrderItem(Product product, Integer quantity, BigDecimal priceAtPurchase) {
        this.product = product;
        this.productName = product.getName();
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}