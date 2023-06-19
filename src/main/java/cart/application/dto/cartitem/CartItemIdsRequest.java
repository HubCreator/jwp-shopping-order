package cart.application.dto.cartitem;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
public class CartItemIdsRequest {

    @NotEmpty
    private List<Long> cartItemIds;

    public CartItemIdsRequest(final List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }
}
