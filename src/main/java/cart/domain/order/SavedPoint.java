package cart.domain.order;

import cart.exception.business.order.InvalidSavedPointException;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
public class SavedPoint {

    private final int savedPoint;

    protected SavedPoint() {
        this.savedPoint = -1;
    }

    public SavedPoint(final int savedPoint) {
        validate(savedPoint);
        this.savedPoint = savedPoint;
    }

    private void validate(final int savedPoint) {
        if (savedPoint < 0) {
            throw new InvalidSavedPointException(savedPoint);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SavedPoint other = (SavedPoint) o;
        return savedPoint == other.savedPoint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(savedPoint);
    }

    @Override
    public String toString() {
        return "SavedPoint{" +
                "savedPoint=" + savedPoint +
                '}';
    }
}
