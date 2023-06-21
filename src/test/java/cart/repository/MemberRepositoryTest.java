package cart.repository;

import cart.domain.member.Member;
import cart.domain.member.MemberEmail;
import cart.domain.member.MemberPoint;
import cart.repository.datajpa.MemberDataJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberDataJpaRepository memberDataJpaRepository;

    @DisplayName("Member를 MemberEmail로 조회한다.")
    @Test
    void findByEmail() {
        // given
        final Member findMember = memberDataJpaRepository.findByEmail(new MemberEmail("a@a.com")).orElseThrow();

        // when, then
        assertAll(
                () -> assertThat(findMember.getId()).isEqualTo(1L),
                () -> assertThat(findMember.getPoint()).isEqualTo(new MemberPoint(10_000))
        );
    }

    @DisplayName("저장된 모든 Member를 조회한다.")
    @Test
    void findAll() {
        // given
        final List<Member> members = memberDataJpaRepository.findAll();

        // when, then
        assertThat(members).hasSize(2);
    }
}
