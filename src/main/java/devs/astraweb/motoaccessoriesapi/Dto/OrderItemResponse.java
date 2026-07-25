package devs.astraweb.motoaccessoriesapi.Dto;

import devs.astraweb.motoaccessoriesapi.model.OrderItem;

import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;
    private String productName;
    private ProductResponse product;
    private Integer quantity;
    private BigDecimal priceAtPurchase;

    public OrderItemResponse() {
    }

    public OrderItemResponse(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.productName = orderItem.getProductName();
        // product is null once the underlying product has been deleted - fall back to the
        // productName snapshot in that case rather than throwing
        this.product = orderItem.getProduct() != null ? new ProductResponse(orderItem.getProduct()) : null;
        this.quantity = orderItem.getQuantity();
        this.priceAtPurchase = orderItem.getPriceAtPurchase();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
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