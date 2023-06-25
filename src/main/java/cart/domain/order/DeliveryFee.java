package cart.domain.order;

import cart.exception.business.order.InvalidDeliveryFeeException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryFee {

    private int deliveryFee;

    public DeliveryFee(final int deliveryFee) {
        validate(deliveryFee);
        this.deliveryFee = deliveryFee;
    }

    public static DeliveryFee none() {
        return new DeliveryFee(0);
    }

    private void validate(final int deliveryFee) {
        if (deliveryFee < 0) {
            throw new InvalidDeliveryFeeException(deliveryFee);
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final DeliveryFee that = (DeliveryFee) other;
        return deliveryFee == that.deliveryFee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryFee);
    }

    @Override
    public String toString() {
        return "DeliveryFee{" +
                "deliveryFee=" + deliveryFee +
                '}';
    }
}
