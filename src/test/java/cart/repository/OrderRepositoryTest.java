package cart.repository;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.order.UsedPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import({OrderRepository.class, CartItemRepository.class,
        ProductRepository.class, OrderProductRepository.class, MemberRepository.class})
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    CartItem cartItem1;
    CartItem cartItem2;
    private CartItems cartItems;
    private Long orderId;

    @BeforeEach
    void setUp() {
        // given
        member = memberRepository.findOne(1L);
        cartItem1 = cartItemRepository.findOne(1L);
        cartItem2 = cartItemRepository.findOne(2L);
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

    @DisplayName("주문에 해당하는 주문 상품을 조회할 수 있다.")
    @Test
    void findAllByOrderId() {
        // given
        final List<OrderProduct> orderProducts = orderRepository.findAllByOrderId(orderId);

        // then
        assertAll(
                () -> assertThat(orderProducts).hasSize(2),
                () -> assertThat(orderProducts.get(0).getProduct()).isEqualTo(cartItem1.getProduct()),
                () -> assertThat(orderProducts.get(1).getProduct()).isEqualTo(cartItem2.getProduct())
        );
    }

    @Test
    void findAllByOrderIds() {
    }

    @Test
    void findAllByMemberId() {
    }

    @Test
    void deleteAll() {
    }
}
