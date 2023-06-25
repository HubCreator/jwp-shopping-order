package cart.domain.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductPrice {

    private int price;
    
    public ProductPrice(final int price) {
        this.price = price;
    }

    public boolean isOverOrEqualThan(final ProductPrice productPrice) {
        return this.price >= productPrice.price;
    }

    public ProductPrice applySale(final double saleRate) {
        return new ProductPrice((int) (price * saleRate));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductPrice other = (ProductPrice) o;
        return price == other.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    @Override
    public String toString() {
        return "ProductPrice{" +
                "price=" + price +
                '}';
    }
}
