package cart.application;

import cart.domain.auth.Auth;
import cart.domain.member.MemberPoint;
import cart.exception.notfound.MemberNotFoundException;
import cart.repository.datajpa.MemberDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberDataJpaRepository memberRepository;

    public MemberPoint getPoint(final Auth auth) {
        return memberRepository.getPointByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
    }
}
