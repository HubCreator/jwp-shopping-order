package cart.application;

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

    public MemberPoint getPoint(final Member member) {
        return memberRepository.findByEmail(member.getEmail())
                .getPoint();
    }
}
