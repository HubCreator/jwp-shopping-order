package cart.ui.dto.order;

import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.UsedPoint;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDetailResponse {

    private final Long orderId;
    private final Integer totalPrice;
    private final Integer usedPoint;
    private final Integer deliveryFee;
    private final LocalDateTime orderedAt;
    private final List<OrderProductDto> products;

    public OrderDetailResponse(final Long orderId, final Integer totalPrice, final UsedPoint usedPoint, final DeliveryFee deliveryFee,
                               final LocalDateTime orderedAt, final List<OrderProductDto> orderProductDtos) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint.getUsedPoint();
        this.deliveryFee = deliveryFee.getDeliveryFee();
        this.orderedAt = orderedAt;
        this.products = orderProductDtos;
    }

    public OrderDetailResponse(final Order order) {
        this.orderId = order.getId();
        this.totalPrice = order.getTotalPrice().getPrice();
        this.usedPoint = order.getUsedPoint().getUsedPoint();
        this.deliveryFee = order.getDeliveryFee().getDeliveryFee();
        this.orderedAt = order.getCreatedDate();
        this.products = order.getOrderProducts().stream()
                .map(OrderProductDto::new)
                .collect(Collectors.toList());
    }

    public String getOrderedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return orderedAt.format(formatter);
    }
}
