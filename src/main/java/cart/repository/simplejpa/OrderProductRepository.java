package cart.repository.simplejpa;

import cart.domain.order.OrderProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderProductRepository {

    private final EntityManager em;

    public List<OrderProduct> findAllByMemberId(final Long memberId) {
        return em.createQuery("select op from OrderProduct op " +
                        "join fetch op.order o " +
                        "where o.member.id = :memberId", OrderProduct.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<OrderProduct> findAllByOrderId(final Long orderId) {
        return em.createQuery("select op from OrderProduct op " +
                        "join fetch op.order o " +
                        "where o.id = :orderId", OrderProduct.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public void deleteByOrderIds(final List<Long> orderIds) {
        em.createQuery("delete OrderProduct op where op.order.id in :ids")
                .setParameter("ids", orderIds)
                .executeUpdate();
        em.clear();
    }
}