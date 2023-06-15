package cart.repository;

import cart.domain.order.OrderProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderProductRepository {

    private final EntityManager em;

    public void saveAll(final List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            em.persist(orderProduct);
        }
    }

    public List<OrderProduct> findAllByMemberId(final Long memberId) {
        return em.createQuery("select op from OrderProduct op where op.order.member = :memberId", OrderProduct.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public void deleteByOrderId(final List<Long> orderIds) {
        em.createQuery("delete OrderProduct op where op.order.id in :ids")
                .setParameter("ids", orderIds)
                .executeUpdate();
        em.clear();
    }
}
