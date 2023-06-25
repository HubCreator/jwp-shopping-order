package cart.domain.order;

import cart.exception.business.order.InvalidSavedPointException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavedPoint {

    private int savedPoint;

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
