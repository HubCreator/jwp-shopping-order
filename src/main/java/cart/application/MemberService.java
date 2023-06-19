package cart.application;

import cart.domain.auth.Auth;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberPoint getPoint(final Auth auth) {
        final Member member = memberRepository.findByEmail(auth.getEmail());
        return memberRepository.findByEmail(member.getEmail())
                .getPoint();
    }
}
