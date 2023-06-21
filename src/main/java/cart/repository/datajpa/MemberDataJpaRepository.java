package cart.repository.datajpa;

import cart.domain.member.Member;
import cart.domain.member.MemberEmail;
import cart.domain.member.MemberPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberDataJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(MemberEmail email);

    @Query("select m.point from Member m where m.email = :email")
    Optional<MemberPoint> getPointByEmail(@Param("email") final MemberEmail email);
}
