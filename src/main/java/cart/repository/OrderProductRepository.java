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
}
