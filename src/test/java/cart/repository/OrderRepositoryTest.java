package cart.repository;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.order.UsedPoint;
import cart.domain.product.Product;
import cart.repository.datajpa.CartItemDataJpaRepository;
import cart.repository.datajpa.MemberDataJpaRepository;
import cart.repository.datajpa.OrderDataJpaRepository;
import cart.repository.datajpa.ProductDataJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private MemberDataJpaRepository memberRepository;
    @Autowired
    private CartItemDataJpaRepository cartItemRepository;
    @Autowired
    private ProductDataJpaRepository productRepository;
    @Autowired
    private OrderDataJpaRepository orderRepository;
    @Autowired
    private EntityManager em;

    private Member member;
    private CartItem cartItem1;
    private CartItem cartItem2;
    private CartItems cartItems;
    private Order order1;

    @BeforeEach
    void setUp() {
        // given
        member = memberRepository.findById(1L).orElseThrow();
        cartItem1 = cartItemRepository.findById(3L).orElseThrow();
        cartItem2 = cartItemRepository.findById(4L).orElseThrow();
        cartItems = new CartItems(List.of(cartItem1, cartItem2));
        final Order order = new Order(member, new UsedPoint(1_000), cartItems.getSavedPoint(), cartItems.getDeliveryFee());
        final List<Product> products = productRepository.findAllById(cartItems.getProductIds());
        final List<OrderProduct> orderProducts = cartItems.toOrderProducts(order, products);
        order.updateOrderProduct(orderProducts);
        this.order1 = orderRepository.save(order);
    }

    @DisplayName("주문을 저장하고 조회한다.")
    @Test
    void saveAndFind() {
        // when, then
        assertAll(
                () -> assertThat(order1.getMember()).isEqualTo(member),
                () -> assertThat(order1.getUsedPoint()).isEqualTo(new UsedPoint(1_000)),
                () -> assertThat(order1.getSavedPoint()).isEqualTo(cartItems.getSavedPoint()),
                () -> assertThat(order1.getDeliveryFee()).isEqualTo(cartItems.getDeliveryFee())
        );
    }

    @DisplayName("주문 번호에 해당하는 주문들을 조회할 수 있다.")
    @Test
    void findAllByOrderIds() {
        // given
        final CartItem cartItem3 = cartItemRepository.findById(5L).orElseThrow();
        final CartItem cartItem4 = cartItemRepository.findById(6L).orElseThrow();
        final CartItems cartItems2 = new CartItems(List.of(cartItem3, cartItem4));
        final Order order2 = new Order(member, new UsedPoint(0), cartItems2.getSavedPoint(), cartItems2.getDeliveryFee());
        final List<Product> products = productRepository.findAllById(cartItems.getProductIds());
        final List<OrderProduct> orderProducts = cartItems.toOrderProducts(order2, products);
        order2.updateOrderProduct(orderProducts);
        orderRepository.save(order2);

        // when
        final List<Order> orders = orderRepository.findAllById(Arrays.asList(1L, order1.getId(), order2.getId()));

        // then
        assertAll(
                () -> assertThat(orders).hasSize(3),
                () -> assertThat(orders.get(0).getOrderProducts()).hasSize(2),
                () -> assertThat(orders.get(1).getOrderProducts()).hasSize(2),
                () -> assertThat(orders.get(2).getOrderProducts()).hasSize(2)
        );
    }

    @DisplayName("특정 회원이 주문한 모든 주문 내역을 확인할 수 있다.")
    @Test
    void findAllByMemberId() {
        // given
        final List<Order> orders = orderRepository.findAllByMemberId(member.getId());

        // when, then
        assertThat(orders).hasSize(2);
    }

    @DisplayName("특정 주문 내역을 삭제할 수 있다.")
    @Test
    void deleteAll() {
        // given
        List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        final int orderSize = orders.size();

        // when
        orderRepository.deleteAll(List.of(order1));

        List<Order> deletedOrders = orderRepository.findAllByMemberId(member.getId());
        final int deletedOrderSize = deletedOrders.size();

        // then
        assertThat(orderSize - 1).isEqualTo(deletedOrderSize);
    }
}
