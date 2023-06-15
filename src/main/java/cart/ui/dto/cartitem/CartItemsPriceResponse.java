package cart.ui.dto.cartitem;

import cart.domain.order.DeliveryFee;
import cart.domain.product.ProductPrice;

public class CartItemsPriceResponse {

    private final Integer totalPrice;
    private final Integer deliveryFee;

    public CartItemsPriceResponse(final ProductPrice totalPrice, final DeliveryFee deliveryFee) {
        this.totalPrice = totalPrice.getPrice();
        this.deliveryFee = deliveryFee.getDeliveryFee();
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }
}
