package cart.domain.member;

import cart.domain.BaseTimeEntity;
import cart.domain.product.ProductPrice;
import cart.exception.business.order.PointAbusedException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Embedded
    private MemberEmail email;
    @Embedded
    private MemberPassword password;
    @Embedded
    private MemberPoint point;

    public Member(final MemberEmail email, final MemberPassword password) {
        this(email, password, null);
    }

    public Member(final MemberEmail email, final MemberPassword password, final MemberPoint point) {
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public Member(final long id, final MemberEmail email, final MemberPassword password) {
        this(id, email, password, MemberPoint.create());
    }

    public Member(final long id, final MemberEmail email, final MemberPassword password, final MemberPoint point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public boolean checkPassword(final MemberPassword password) {
        return this.password.equals(password);
    }

    public Member updatePoint(final MemberPoint requestedPoint, final ProductPrice productTotalPrice) {
        if (point.isLowerThan(requestedPoint)) {
            throw new PointAbusedException(point, requestedPoint);
        }
        final MemberPoint minusPoint = point.minus(requestedPoint);
        final MemberPoint resultPoint = minusPoint.addPointByTotalPrice(productTotalPrice);

        return new Member(id, email, password, resultPoint);
    }

    public String getEmailValue() {
        return email.getEmail();
    }

    public String getPasswordValue() {
        return password.getPassword();
    }


    public Integer getPointValue() {
        return point.getPoint();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email=" + email +
                ", password=" + password +
                ", point=" + point +
                '}';
    }
}
