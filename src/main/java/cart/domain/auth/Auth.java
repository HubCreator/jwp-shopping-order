package cart.domain.auth;

import cart.domain.member.MemberEmail;
import cart.domain.member.MemberPassword;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Auth {

    private final MemberEmail email;
    private final MemberPassword password;

    public boolean checkPassword(final MemberPassword password) {
        return this.password.equals(password);
    }

    public MemberEmail getEmail() {
        return email;
    }
}

