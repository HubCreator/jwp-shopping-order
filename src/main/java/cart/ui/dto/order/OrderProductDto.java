package cart.ui.dto.order;

import cart.domain.order.OrderProduct;
import cart.ui.dto.product.ProductResponse;

public class OrderProductDto {

    private final Integer quantity;
    private final ProductResponse product;

    public OrderProductDto(final Long id, final String name, final Integer price, final String imageUrl, final Integer quantity) {
        this.quantity = quantity;
        this.product = new ProductResponse(id, name, price, imageUrl);
    }

    public OrderProductDto(final OrderProduct orderProduct) {
        this.quantity = orderProduct.getQuantityValue();
        this.product = new ProductResponse(
                orderProduct.getId(),
                orderProduct.getProductNameValue(),
                orderProduct.getProductPriceValue(),
                orderProduct.getProductImageUrlValue());
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
