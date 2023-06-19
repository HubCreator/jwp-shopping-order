package cart.ui.dto.cartitem;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemsPriceResponse {

    private int totalPrice;
    private int deliveryFee;

    public CartItemsPriceResponse(final TotalPriceAndDeliveryFeeDto dto) {
        this.totalPrice = dto.getTotalPrice();
        this.deliveryFee = dto.getDeliveryFee();
    }
}
