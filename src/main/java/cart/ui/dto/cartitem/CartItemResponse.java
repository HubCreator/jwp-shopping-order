package cart.ui.dto.cartitem;

import cart.domain.cartitem.CartItem;
import cart.ui.dto.product.ProductResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemResponse {

    private Long id;
    private int quantity;
    private ProductResponse product;

    public CartItemResponse(final CartItem cartItem) {
        this.id = cartItem.getId();
        this.quantity = cartItem.getQuantityValue();
        this.product = new ProductResponse(cartItem.getProduct());
    }
}
