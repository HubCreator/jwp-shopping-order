package cart.ui.dto.order;

import cart.domain.order.Order;
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
    private final List<OrderProductResponse> products;

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

    public OrderDetailResponse(final OrderAndOrderProductsDto dto) {
        this.orderId = dto.getOrder().getId();
        this.totalPrice = dto.getOrderProducts().stream()
                .mapToInt(orderProduct -> orderProduct.getProductPriceValue() * orderProduct.getQuantityValue())
                .sum();
        this.usedPoint = dto.getOrder().getUsedPointValue();
        this.deliveryFee = dto.getOrder().getDeliveryFeeValue();
        this.orderedAt = dto.getOrder().getCreatedDate();
        this.products = dto.getOrderProducts().stream()
                .map(OrderProductResponse::new)
                .collect(Collectors.toList());
    }

    public String getOrderedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return orderedAt.format(formatter);
    }
}
