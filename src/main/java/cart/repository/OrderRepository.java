package cart.repository;

import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.order.UsedPoint;
import cart.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
        em.persist(order);
        final List<Product> products = productRepository.findAllByIds(cartItems.getProductIds());
        final List<OrderProduct> orderProducts = cartItems.toOrderProducts(order, products);
        orderProductRepository.saveAll(orderProducts);
        return order.getId();
    }

    public Order findOne(final Long id) {
        return em.find(Order.class, id);
    }

    public List<OrderProduct> findAllByOrderId(final Long orderId) {
        return em.createQuery("select op from OrderProduct op join op.order o where o.id = :orderId", OrderProduct.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<Order> findAllByOrderIds(final List<Long> ids) {
//        em.createQuery("select distinct o from Order o join fetch o.orderProducts where o.id in :ids", Order.class)
        return em.createQuery("select o from OrderProduct op join fetch op.order o where o.id in :ids", Order.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    public List<Order> findAllByMemberId(final Long memberId) {
        return em.createQuery("select o from Order o where o.member = :memberId", Order.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public void deleteAll(final List<Order> orders) {
        final List<Order> findOrders = orders.stream()
                .map(m -> findOne(m.getId()))
                .collect(Collectors.toList());
        findOrders.forEach(order -> em.remove(order));
    }
}
