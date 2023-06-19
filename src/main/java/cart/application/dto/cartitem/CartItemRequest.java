package cart.application.dto.cartitem;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class CartItemRequest {

    @NotNull
    @Positive
    private Long productId;

    public CartItemRequest(final Long productId) {
        this.productId = productId;
    }
}
