package cart.repository.simplejpa;

import cart.domain.auth.Auth;
import cart.domain.member.Member;
import cart.domain.member.MemberEmail;
import cart.exception.notfound.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository
@RequiredArgsConstructor
public class AuthRepository {

    private final EntityManager em;

    public Auth findByEmail(final MemberEmail email) {
        try {
            final Member member = em.createQuery("select m from Member m where m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return new Auth(member.getEmail(), member.getPassword());
        } catch (final NoResultException e) {
            throw new MemberNotFoundException(email);
        }
    }
}
