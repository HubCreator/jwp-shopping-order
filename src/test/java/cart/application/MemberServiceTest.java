package cart.application;

import cart.domain.auth.Auth;
import cart.domain.member.MemberEmail;
import cart.domain.member.MemberPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @DisplayName("멤버의 포인트를 조회할 수 있다.")
    @Test
    void getMemberPoint() {
        // given
        final MemberPoint point = memberService.getPoint(new Auth(new MemberEmail("a@a.com"), null));

        // when, then
        assertThat(point).isEqualTo(new MemberPoint(10_000));
    }
}
