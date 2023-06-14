package cart.domain.member;

import cart.exception.business.member.InvalidMemberEmailException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MemberEmail {

    private final String email;

    protected MemberEmail() {
        this.email = null;
    }

    public MemberEmail(final String email) {
        validate(email);
        this.email = email;
    }

    private void validate(final String email) {
        if (email.isBlank() || !email.contains("@")) {
            throw new InvalidMemberEmailException(email);
        }
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberEmail other = (MemberEmail) o;
        return Objects.equals(email, other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "MemberEmail{" +
                "email='" + email + '\'' +
                '}';
    }
}
