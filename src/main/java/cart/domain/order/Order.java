package cart.domain.order;

import cart.domain.BaseTimeEntity;
import cart.domain.member.Member;
import cart.exception.authorization.OrderAccessForbiddenException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "order")
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
