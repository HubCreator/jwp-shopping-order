package cart.repository;

import cart.domain.member.Member;
import cart.exception.notfound.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(final Member member) {
        if (member.getId() == null) {
            em.persist(member);
            return;
        }
        em.merge(member);
    }

    public Member findOne(final Long id) {
        return em.find(Member.class, id);
    }

    public Member findByEmail(final String email) {
        try {
            return em.createQuery("select m from Member m where m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (final NoResultException | NonUniqueResultException e) {
            throw new MemberNotFoundException(email);
        }
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
