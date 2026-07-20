package devs.astraweb.motoaccessoriesapi.Dto;

import devs.astraweb.motoaccessoriesapi.model.CartItem;

public class CartItemResponse {

    private Long id;
    private ProductResponse product;
    private Integer quantity;

    public CartItemResponse() {
    }

    public CartItemResponse(CartItem cartItem) {
        this.id = cartItem.getId();
        this.product = new ProductResponse(cartItem.getProduct());
        this.quantity = cartItem.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
