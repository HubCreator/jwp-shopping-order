package cart.domain.product;

import lombok.Getter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
public class Product {

    private static final long NOT_YET_PERSIST_ID = -1;

    @Id
    @GeneratedValue
    private Long id;
    @Embedded
    private ProductName productName;
    @Embedded
    private ProductPrice productPrice;
    @Embedded
    private ProductImageUrl productImageUrl;

    protected Product() {
    }

    public Product(final ProductName productName, final ProductPrice productPrice, final ProductImageUrl productImageUrl) {
        this(NOT_YET_PERSIST_ID, productName, productPrice, productImageUrl);
    }

    public Product(final long id, final ProductName productName,
                   final ProductPrice productPrice, final ProductImageUrl productImageUrl) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
    }


    public String getNameValue() {
        return productName.getName();
    }

    public int getPriceValue() {
        return productPrice.getPrice();
    }

    public String getImageUrlValue() {
        return productImageUrl.getImageUrl();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName=" + productName +
                ", productPrice=" + productPrice +
                ", productImageUrl=" + productImageUrl +
                '}';
    }
}
