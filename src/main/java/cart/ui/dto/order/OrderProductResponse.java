package cart.ui.dto.order;

import cart.domain.order.OrderProduct;
import cart.ui.dto.product.ProductResponse;
import lombok.Data;

@Data
public class OrderProductResponse {

    private final Integer quantity;
    private final ProductResponse product;

    public OrderProductResponse(final OrderProduct orderProduct) {
        this.quantity = orderProduct.getQuantityValue();
        this.product = new ProductResponse(orderProduct.getProduct());
    }
}
