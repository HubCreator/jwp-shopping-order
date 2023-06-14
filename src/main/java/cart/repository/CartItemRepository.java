package cart.repository;

import cart.domain.cartitem.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CartItemRepository {

    private final EntityManager em;

    public void save(final CartItem cartItem) {
        em.persist(cartItem);
    }

    public CartItem findById(final Long id) {
        return em.find(CartItem.class, id);
    }

    public List<CartItem> findAllByMemberId(final Long memberId) {
        return em.createQuery("select c from CartItem c where c.member = :memberId", CartItem.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<CartItem> findAllByIds(final List<Long> ids) {
        return em.createQuery("select c from CartItem  c where c.id in :ids", CartItem.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    public void update(final CartItem cartItem) {
        final CartItem findCartItem = em.find(CartItem.class, cartItem.getId());
        findCartItem.update(cartItem);
    }

    public void delete(final CartItem cartItem) {
        em.remove(cartItem);
    }

    public void deleteAll(final List<CartItem> cartItems) {
        final List<Long> ids = cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
        em.createQuery("delete from CartItem c where c.id in :ids")
                .setParameter("ids", ids)
                .executeUpdate();
    }

    public void deleteByIds(final List<Long> ids) {
        em.createQuery("delete from CartItem c where c.id in :ids")
                .setParameter("ids", ids)
                .executeUpdate();
    }
}
