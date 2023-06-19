package cart.application.dto.cartitem;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
public class CartItemQuantityUpdateRequest {

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    public CartItemQuantityUpdateRequest(final int quantity) {
        this.quantity = quantity;
    }
}
