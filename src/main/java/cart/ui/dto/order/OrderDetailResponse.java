package cart.ui.dto.order;

import cart.domain.order.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
public class OrderDetailResponse {

    private Long orderId;
    private Integer totalPrice;
    private Integer usedPoint;
    private Integer deliveryFee;
    private LocalDateTime orderedAt;
    private List<OrderProductResponse> products;

    public OrderDetailResponse(final Order order) {
        this.orderId = order.getId();
        this.totalPrice = order.getTotalPrice().getPrice();
        this.usedPoint = order.getUsedPoint().getUsedPoint();
        this.deliveryFee = order.getDeliveryFee().getDeliveryFee();
        this.orderedAt = order.getCreatedDate();
        this.products = order.getOrderProducts().stream()
                .map(OrderProductResponse::new)
                .collect(Collectors.toList());
    }

    public String getOrderedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return orderedAt.format(formatter);
    }
}
