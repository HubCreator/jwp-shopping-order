package cart.repository.simplejpa;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.exception.notfound.CartItemNotFoundException;
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

    public CartItem findOne(final Long id) {
        final CartItem cartItem = em.find(CartItem.class, id);
        if (cartItem == null) {
            throw new CartItemNotFoundException(id);
        }
        return cartItem;
    }

    public List<CartItem> findAllByMember(final Member member) {
        return em.createQuery("select c from CartItem c " +
                        "join c.member m " +
                        "join fetch c.product p " +
                        "where m.id = :memberId", CartItem.class)
                .setParameter("memberId", member.getId())
                .getResultList();
    }

    public List<CartItem> findAllByIds(final List<Long> ids) {
        return em.createQuery("select c from CartItem c " +
                        "join fetch c.member m " +
                        "where c.id in :ids", CartItem.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    public void updateQuantity(final CartItem cartItem, final Quantity quantity) {
        final CartItem findCartItem = findOne(cartItem.getId());
        findCartItem.updateQuantity(quantity);
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
        em.clear();
    }

    public void deleteByIds(final List<Long> ids) {
        em.createQuery("delete from CartItem c where c.id in :ids")
                .setParameter("ids", ids)
                .executeUpdate();
        em.clear();
    }
}
