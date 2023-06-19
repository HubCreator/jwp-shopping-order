package cart.ui.dto.order;

import cart.domain.order.OrderProduct;
import cart.ui.dto.product.ProductResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderProductResponse {

    private int quantity;
    private ProductResponse product;

    public OrderProductResponse(final OrderProduct orderProduct) {
        this.quantity = orderProduct.getQuantityValue();
        this.product = new ProductResponse(orderProduct.getProduct());
    }
}
