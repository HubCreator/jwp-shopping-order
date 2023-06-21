package cart.repository.datajpa;

import cart.domain.cartitem.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemDataJpaRepository extends JpaRepository<CartItem, Long> {

    @Query("select c from CartItem c " +
            "join c.member m " +
            "join fetch c.product p " +
            "where m.id = :memberId")
    List<CartItem> findAllByMemberId(@Param("memberId") final Long memberId);

    @EntityGraph(attributePaths = {"member"})
    @Override
    List<CartItem> findAllById(Iterable<Long> longs);
}
