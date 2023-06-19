package cart.ui.dto.cartitem;

import cart.domain.order.DeliveryFee;
import cart.domain.product.ProductPrice;
import lombok.Data;

@Data
public class TotalPriceAndDeliveryFeeDto {
    private final int totalPrice;
    private final int deliveryFee;

    public TotalPriceAndDeliveryFeeDto(final ProductPrice totalPrice, final DeliveryFee deliveryFee) {
        this.totalPrice = totalPrice.getPrice();
        this.deliveryFee = deliveryFee.getDeliveryFee();
    }
}
