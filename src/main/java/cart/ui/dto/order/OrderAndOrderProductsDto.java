package cart.ui.dto.order;

import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import lombok.Data;

import java.util.List;

@Data
public class OrderAndOrderProductsDto {

    private final Order order;
    private final List<OrderProduct> orderProducts;

    public OrderAndOrderProductsDto(final Order order, final List<OrderProduct> orderProducts) {
        this.order = order;
        this.orderProducts = orderProducts;
    }
}
