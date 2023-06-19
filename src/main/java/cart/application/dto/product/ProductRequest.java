package cart.application.dto.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
public class ProductRequest {

    @NotEmpty
    private String name;
    @NotNull
    @PositiveOrZero
    private Integer price;
    @NotEmpty
    private String imageUrl;

    public ProductRequest(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
