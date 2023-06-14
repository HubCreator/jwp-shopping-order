package cart.domain.order;

import cart.exception.business.order.InvalidDeliveryFeeException;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
public class DeliveryFee {

    private final int deliveryFee;

    protected DeliveryFee() {
        this.deliveryFee = -1;
    }

    public DeliveryFee(final int deliveryFee) {
        validate(deliveryFee);
        this.deliveryFee = deliveryFee;
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
