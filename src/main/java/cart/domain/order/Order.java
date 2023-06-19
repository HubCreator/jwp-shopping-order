package cart.domain.order;

import cart.domain.BaseTimeEntity;
import cart.domain.member.Member;
import cart.domain.product.ProductPrice;
import cart.exception.authorization.OrderAccessForbiddenException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Embedded
    private UsedPoint usedPoint;
    @Embedded
    private SavedPoint savedPoint;
    @Embedded
    private DeliveryFee deliveryFee;

    public Order(final Member member, final UsedPoint usedPoint,
                 final SavedPoint savedPoint, final DeliveryFee deliveryFee) {
        this.member = member;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.deliveryFee = deliveryFee;
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member, member)) {
            throw new OrderAccessForbiddenException(member.getEmail());
        }
    }

    public void updateOrderProduct(final List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public ProductPrice getTotalPrice() {
        final int totalPrice = orderProducts.stream()
                .mapToInt(op -> op.getProductPrice().getPrice() * op.getQuantityValue())
                .sum();
        return new ProductPrice(totalPrice);
    }

    public Integer getUsedPointValue() {
        return usedPoint.getUsedPoint();
    }

    public Integer getDeliveryFeeValue() {
        return deliveryFee.getDeliveryFee();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", orderProducts=" + orderProducts +
                ", usedPoint=" + usedPoint +
                ", savedPoint=" + savedPoint +
                ", deliveryFee=" + deliveryFee +
                '}';
    }
}
