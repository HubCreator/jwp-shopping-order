package cart.domain.product;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
public class ProductPrice {

    private final int price;

    protected ProductPrice() {
        this.price = -1;
    }

    public ProductPrice(final int price) {
        this.price = price;
    }

    public boolean isOverOrEqualThan(final ProductPrice productPrice) {
        return this.price <= productPrice.price;
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
