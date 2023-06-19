package cart.repository;

import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.order.UsedPoint;
import cart.domain.product.Product;
import cart.exception.notfound.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    public Long save(final CartItems cartItems, final Member member, final UsedPoint usedPoint) {
        final Order order = new Order(member, usedPoint, cartItems.getSavedPoint(), cartItems.getDeliveryFee());
        final List<Product> products = productRepository.findAllByIds(cartItems.getProductIds());
        final List<OrderProduct> orderProducts = cartItems.toOrderProducts(order, products);
        order.updateOrderProduct(orderProducts);
        em.persist(order);
        return order.getId();
    }

    public Order findOne(final Long id) {
        final Order order = em.find(Order.class, id);
        if (order == null) {
            throw new OrderNotFoundException(id);
        }
        return order;
    }

    public Order findOneWithOrderItems(final Long id) {
        try {
            return em.createQuery("select o from Order o " +
                            "join fetch o.orderProducts op " +
                            "where o.id = :id", Order.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (final NoResultException e) {
            throw new OrderNotFoundException(id);
        }
    }

    public List<Order> findAllByOrderIds(final List<Long> ids) {
        return em.createQuery("select o from Order o " +
                        "join fetch o.member m " +
                        "where o.id in :ids", Order.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    public List<Order> findAllByMemberId(final Long memberId) {
        return em.createQuery("select o from Order o " +
                        "join fetch o.member m " +
                        "where m.id = :memberId", Order.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public void deleteAll(final List<Order> orders) {
        final List<Long> ids = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());
        orderProductRepository.deleteByOrderIds(ids);

        em.createQuery("delete Order o where o.id in :ids")
                .setParameter("ids", ids)
                .executeUpdate();
        em.clear();
    }
}
