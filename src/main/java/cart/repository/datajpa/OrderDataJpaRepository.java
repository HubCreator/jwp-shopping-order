package cart.repository.datajpa;

import cart.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderDataJpaRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "join fetch o.orderProducts op " +
            "where o.id = :id")
    Optional<Order> findByIdWithOrderItems(@Param("id") final Long id);


    @Query("select o from Order o " +
            "join fetch o.member m " +
            "where m.id = :memberId")
    List<Order> findAllByMemberId(@Param("memberId") final Long memberId);

    @Query("select o from Order o " +
            "join fetch o.member m " +
            "where o.id in :ids")
    List<Order> findAllByOrderIds(@Param("ids") final List<Long> ids);
}
