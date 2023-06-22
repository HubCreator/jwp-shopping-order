package cart.repository.datajpa;

import cart.domain.cartitem.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemDataJpaRepository extends JpaRepository<CartItem, Long> {

    @EntityGraph(attributePaths = {"product"})
    @Override
    Optional<CartItem> findById(final Long id);

    @Query("select c from CartItem c " +
            "join fetch c.member m " +
            "join fetch c.product p " +
            "where m.id = :memberId")
    List<CartItem> findAllByMemberId(@Param("memberId") final Long memberId);

    @EntityGraph(attributePaths = {"member"})
    @Override
    List<CartItem> findAllById(final Iterable<Long> ids);
}
