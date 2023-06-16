package cart.exception.notfound;

import cart.domain.member.MemberEmail;

public final class MemberNotFoundException extends NotFoundException {

    private static final String INVALID_EMAIL_MESSAGE = "해당 사용자가 존재하지 않습니다. 입력한 이메일: %s";
    private static final String INVALID_ID_MESSAGE = "해당 사용자가 존재하지 않습니다. 입력한 id: %d";

    public MemberNotFoundException(final MemberEmail email, final Throwable e) {
        super(String.format(INVALID_EMAIL_MESSAGE, email.getEmail()), e);
    }

    public MemberNotFoundException(final Long id) {
        super(String.format(INVALID_ID_MESSAGE, id));
    }
}
