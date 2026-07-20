package devs.astraweb.motoaccessoriesapi.Dto;

import jakarta.validation.constraints.NotNull;

public class WishlistItemRequest {

    @NotNull
    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
