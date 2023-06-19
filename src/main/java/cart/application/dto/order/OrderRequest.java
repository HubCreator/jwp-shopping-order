package cart.application.dto.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderRequest {

    @NotEmpty
    private List<Long> cartItemIds;
    @NotNull
    @PositiveOrZero
    private Integer point;

    public OrderRequest(final List<Long> cartItemIds, final Integer point) {
        this.cartItemIds = cartItemIds;
        this.point = point;
    }
}
