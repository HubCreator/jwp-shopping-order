package cart.ui.dto.order;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PointResponse {

    private int point;

    public PointResponse(final int point) {
        this.point = point;
    }
}
