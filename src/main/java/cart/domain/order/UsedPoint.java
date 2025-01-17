package cart.domain.order;

import cart.exception.business.order.InvalidUsedPointException;

import java.util.Objects;

public class UsedPoint {

    private final int usedPoint;

    public UsedPoint(final int usedPoint) {
        validate(usedPoint);
        this.usedPoint = usedPoint;
    }

    private void validate(final int usedPoint) {
        if (usedPoint < 0) {
            throw new InvalidUsedPointException(usedPoint);
        }
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UsedPoint other = (UsedPoint) o;
        return usedPoint == other.usedPoint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(usedPoint);
    }

    @Override
    public String toString() {
        return "UsedPoint{" +
                "usedPoint=" + usedPoint +
                '}';
    }
}
