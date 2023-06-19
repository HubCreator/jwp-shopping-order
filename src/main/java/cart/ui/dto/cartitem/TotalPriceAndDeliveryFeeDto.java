package cart.ui.dto.cartitem;

import cart.domain.order.DeliveryFee;
import cart.domain.product.ProductPrice;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TotalPriceAndDeliveryFeeDto {

    private int totalPrice;
    private int deliveryFee;

    public TotalPriceAndDeliveryFeeDto(final ProductPrice totalPrice, final DeliveryFee deliveryFee) {
        this.totalPrice = totalPrice.getPrice();
        this.deliveryFee = deliveryFee.getDeliveryFee();
    }
}
