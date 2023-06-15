package cart.ui.dto.order;

import cart.domain.order.DeliveryFee;
import cart.domain.order.UsedPoint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public Long getOrderId() {
        return orderId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    public String getOrderedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return orderedAt.format(formatter);
    }

    public List<OrderProductDto> getProducts() {
        return products;
    }
}
