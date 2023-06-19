package cart.ui.dto.cartitem;

import lombok.Data;

@Data
public class CartItemsPriceResponse {

    private final int totalPrice;
    private final int deliveryFee;

    public CartItemsPriceResponse(final TotalPriceAndDeliveryFeeDto dto) {
        this.totalPrice = dto.getTotalPrice();
        this.deliveryFee = dto.getDeliveryFee();
    }
}
