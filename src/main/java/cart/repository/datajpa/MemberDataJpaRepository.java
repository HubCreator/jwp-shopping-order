package cart.repository.datajpa;

import cart.domain.member.Member;
import cart.domain.member.MemberEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberDataJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(MemberEmail email);
}
