package cart.domain.order;

import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.exception.authorization.OrderException;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private Long id;
    private final Member member;
    private final MemberPoint usedPoint;
    private final MemberPoint savedPoint;
    private final DeliveryFee deliveryFee;
    private LocalDateTime orderedAt;

    public Order(final Member member, final MemberPoint usedPoint, final MemberPoint savedPoint, final DeliveryFee deliveryFee) {
        this.member = member;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.deliveryFee = deliveryFee;
    }

    public Order(final Long id, final Member member,
                 final MemberPoint usedPoint,
                 final MemberPoint savedPoint,
                 final DeliveryFee deliveryFee,
                 final LocalDateTime orderedAt) {
        this.id = id;
        this.member = member;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.deliveryFee = deliveryFee;
        this.orderedAt = orderedAt;
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member, member)) {
            throw new OrderException(member.getEmail());
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public MemberPoint getUsedPoint() {
        return usedPoint;
    }

    public Integer getUsedPointValue() {
        return usedPoint.getPoint();
    }

    public MemberPoint getSavedPoint() {
        return savedPoint;
    }

    public Integer getSavedPointValue() {
        return savedPoint.getPoint();
    }

    public DeliveryFee getDeliveryFee() {
        return deliveryFee;
    }

    public int getDeliveryFeeValue() {
        return deliveryFee.getDeliveryFee();
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
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
                ", usedPoint=" + usedPoint +
                ", savedPoint=" + savedPoint +
                ", deliveryFee=" + deliveryFee +
                ", orderedAt=" + orderedAt +
                '}';
    }
}