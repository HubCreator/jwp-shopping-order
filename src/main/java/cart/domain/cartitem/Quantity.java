package cart.domain.cartitem;

import cart.exception.business.cartitem.InvalidCartItemQuantityException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quantity {

    private static final int INITIAL_VALUE = 1;

    private int quantity;

    public Quantity(final int quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    public static Quantity create() {
        return new Quantity(INITIAL_VALUE);
    }

    private void validate(final int quantity) {
        if (quantity < 0) {
            throw new InvalidCartItemQuantityException(quantity);
        }
    }

    public Quantity add() {
        return new Quantity(quantity + 1);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Quantity other = (Quantity) o;
        return quantity == other.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }

    @Override
    public String toString() {
        return "Quantity{" +
                "quantity=" + quantity +
                '}';
    }
}
