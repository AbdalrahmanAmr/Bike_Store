package devs.astraweb.motoaccessoriesapi.Dto;

import devs.astraweb.motoaccessoriesapi.model.WishlistItem;

public class WishlistItemResponse {

    private Long id;
    private ProductResponse product;

    public WishlistItemResponse() {
    }

    public WishlistItemResponse(WishlistItem wishlistItem) {
        this.id = wishlistItem.getId();
        this.product = new ProductResponse(wishlistItem.getProduct());
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
}
