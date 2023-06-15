package cart.repository;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.UsedPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    private Member member;
    CartItem cartItem1;
    CartItem cartItem2;
    private CartItems cartItems;
    private Long orderId;

    @BeforeEach
    void setUp() {
        // given
        member = memberRepository.findOne(1L);
        cartItem1 = cartItemRepository.findOne(3L);
        cartItem2 = cartItemRepository.findOne(4L);
        cartItems = new CartItems(List.of(cartItem1, cartItem2));
        orderId = orderRepository.save(cartItems, member, new UsedPoint(1_000));
    }

    @DisplayName("주문을 저장하고 조회한다.")
    @Test
    void saveAndFind() {
        // when
        final Order order = orderRepository.findOne(orderId);

        // then
        assertAll(
                () -> assertThat(order.getMember()).isEqualTo(member),
                () -> assertThat(order.getUsedPoint()).isEqualTo(new UsedPoint(1_000)),
                () -> assertThat(order.getSavedPoint()).isEqualTo(cartItems.getSavedPoint()),
                () -> assertThat(order.getDeliveryFee()).isEqualTo(cartItems.getDeliveryFee())
        );
    }

    @DisplayName("주문 번호에 해당하는 주문들을 조회할 수 있다.")
    @Test
    void findAllByOrderIds() {
        // given
        final CartItem cartItem3 = cartItemRepository.findOne(5L);
        final CartItem cartItem4 = cartItemRepository.findOne(6L);
        final CartItems cartItems2 = new CartItems(List.of(cartItem3, cartItem4));
        final Long orderId2 = orderRepository.save(cartItems2, member, new UsedPoint(0));

        // when
        final List<Order> orders = orderRepository.findAllByOrderIds(List.of(1L, orderId, orderId2));

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
        final Order order1 = orderRepository.findOne(1L);
        final Order order2 = orderRepository.findOne(2L);
        orderRepository.deleteAll(List.of(order1, order2));

        List<Order> deletedOrders = orderRepository.findAllByMemberId(member.getId());
        final int deletedOrderSize = deletedOrders.size();

        // then
        assertThat(orderSize - 2).isEqualTo(deletedOrderSize);
    }
}
