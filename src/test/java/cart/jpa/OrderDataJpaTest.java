package cart.jpa;

import cart.domain.order.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
@SpringBootTest
public class OrderDataJpaTest {

    @Autowired
    private EntityManager em;

    @Test
    void hello() {
        // given
        Order order = em.find(Order.class, 1L);

        // when
        /*List<Order> orders = em.createQuery("select distinct o from Order o join fetch o.orderProducts where o.id = 1", Order.class)
                .getResultList();*/
        List<Order> orders = em.createQuery("select distinct o from Order o join fetch o.orderProducts where o.id in :ids", Order.class)
                .setParameter("ids", List.of(1L))
                .getResultList();

        System.out.println("orders = " + orders);

        System.out.println("================================");
        // then
        for (Order o : orders) {
            System.out.println("o = " + o);
        }
    }
}
