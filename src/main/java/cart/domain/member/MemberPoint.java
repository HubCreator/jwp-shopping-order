package cart.domain.member;

import cart.domain.product.ProductPrice;
import cart.exception.business.member.InvalidMemberPointException;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
public class MemberPoint {

    private static final double APPLICATION_RATE = 0.1;

    private final int point;

    protected MemberPoint() {
        this.point = -1;
    }

    public MemberPoint(final Integer point) {
        validate(point);
        this.point = point;
    }

    public static MemberPoint empty() {
        return new MemberPoint(0);
    }

    private void validate(final Integer point) {
        if (point < 0) {
            throw new InvalidMemberPointException();
        }
    }

    public MemberPoint minus(final MemberPoint point) {
        return new MemberPoint(this.point - point.point);
    }

    public MemberPoint addPointByTotalPrice(final ProductPrice totalPrice) {
        return new MemberPoint(point + (int) (totalPrice.getPrice() * APPLICATION_RATE));
    }

    public boolean isLowerThan(final MemberPoint point) {
        return this.point < point.point;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberPoint other = (MemberPoint) o;
        return Objects.equals(point, other.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }

    @Override
    public String toString() {
        return "MemberPoint{" +
                "point=" + point +
                '}';
    }
}
