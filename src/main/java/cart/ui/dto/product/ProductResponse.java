package cart.ui.dto.product;

import cart.domain.product.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    public ProductResponse(final Product product) {
        this.id = product.getId();
        this.name = product.getNameValue();
        this.price = product.getPriceValue();
        this.imageUrl = product.getImageUrlValue();
    }
}
