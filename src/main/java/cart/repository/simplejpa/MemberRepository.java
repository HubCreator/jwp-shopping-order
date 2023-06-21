package cart.repository.simplejpa;

import cart.domain.member.Member;
import cart.domain.member.MemberEmail;
import cart.exception.notfound.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(final Member member) {
        em.persist(member);
    }

    public Member findOne(final Long id) {
        final Member member = em.find(Member.class, id);
        if (member == null) {
            throw new MemberNotFoundException(id);
        }
        return member;
    }

    public Member findByEmail(final MemberEmail email) {
        try {
            return em.createQuery("select m from Member m where m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (final NoResultException e) {
            throw new MemberNotFoundException(email);
        }
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
