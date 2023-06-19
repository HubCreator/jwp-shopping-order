package cart.repository;

import cart.domain.auth.Auth;
import cart.domain.member.Member;
import cart.domain.member.MemberEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class AuthRepository {

    private final EntityManager em;
    private final MemberRepository memberRepository;


    public Auth findByEmail(final MemberEmail email) {
        final Member member = memberRepository.findByEmail(email);
        return new Auth(member.getEmail(), member.getPassword());
    }
}
