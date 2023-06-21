package cart.domain.order;

import cart.domain.cartitem.Quantity;
import cart.domain.common.BaseTimeEntity;
import cart.domain.product.Product;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private ProductName productName;
    @Embedded
    private ProductPrice productPrice;
    @Embedded
    private ProductImageUrl productImageUrl;
    @Embedded
    private Quantity quantity;

    public OrderProduct(final Order order, final Product product,
                        final ProductName productName, final ProductPrice productPrice,
                        final ProductImageUrl productImageUrl, final Quantity quantity) {
        this.order = order;
        this.product = product;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return product.getId();
    }

    public String getProductImageUrlValue() {
        return productImageUrl.getImageUrl();
    }

    public int getQuantityValue() {
        return quantity.getQuantity();
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderProduct that = (OrderProduct) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", productName=" + productName +
                ", productPrice=" + productPrice +
                ", productImageUrl=" + productImageUrl +
                ", quantity=" + quantity +
                '}';
    }
}
