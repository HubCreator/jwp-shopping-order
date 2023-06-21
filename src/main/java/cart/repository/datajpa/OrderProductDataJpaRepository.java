package cart.repository.datajpa;

import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductDataJpaRepository extends JpaRepository<OrderProduct, Long> {

    @EntityGraph(attributePaths = {"order"})
    List<OrderProduct> findAllByOrder(final Order order);


    @EntityGraph(attributePaths = {"order"})
    List<OrderProduct> findAllByOrderMember(final Member member);
}
