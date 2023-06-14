package cart.domain.product;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
public class ProductImageUrl {

    private final String imageUrl;

    protected ProductImageUrl() {
        this.imageUrl = null;
    }

    public ProductImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductImageUrl other = (ProductImageUrl) o;
        return Objects.equals(imageUrl, other.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }

    @Override
    public String toString() {
        return "ProductImageUrl{" +
                "imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
