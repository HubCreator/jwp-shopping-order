package cart.application;

import cart.application.dto.order.OrderRequest;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.order.SavedPoint;
import cart.domain.order.UsedPoint;
import cart.domain.product.Product;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import cart.exception.business.order.InvalidPointUseException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderProductRepository;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

    @Nested
    @DisplayName("상품을 주문할 때에는")
    class DescribeOrderMethodTest1 {

        private Member member;
        private Product product1;
        private Product product2;

        @BeforeEach
        void setUp() {
            member = memberRepository.findOne(1L);
            product1 = productRepository.findOne(1L);
            product2 = productRepository.findOne(2L);
        }

        @Nested
        @DisplayName("만약 총 주문 가격이 5이상 초과이라면")
        class ContextWithDiscountDeliveryFee {
            private final Quantity quantity1 = new Quantity(1);
            private final Quantity quantity2 = new Quantity(2);
            private final int usedPoint = 1000;
            private Order order;

            @BeforeEach
            void setUp() {
                final CartItem cartItem1 = new CartItem(member, product1, quantity1);
                final CartItem cartItem2 = new CartItem(member, product2, quantity2);
                cartItemRepository.save(cartItem1);
                cartItemRepository.save(cartItem2);

                final OrderRequest orderRequest = new OrderRequest(List.of(cartItem1.getId(), cartItem2.getId()), usedPoint);
                final Long orderId = orderService.order(member, orderRequest);
                order = orderRepository.findOne(orderId);
            }

            @DisplayName("배송비 3천원을 추가하지 않는다.")
            @Test
            void it_returns_discounted_delivery_fee() {
                assertAll(
                        () -> assertThat(member.getPoint()).isEqualTo(new MemberPoint(14000)),
                        () -> assertThat(order.getDeliveryFee()).isEqualTo(new DeliveryFee(0)),
                        () -> assertThat(order.getUsedPoint()).isEqualTo(new UsedPoint(1000))
                );
            }
        }

        @Nested
        @DisplayName("만약 총 주문 가격이 5만원 미만이라면")
        class ContextWithNonDiscountDeliveryFee {
            private final Quantity quantity1 = new Quantity(1);
            private final Quantity quantity2 = new Quantity(1);
            private final int usedPoint = 1000;
            private Order order;


            @BeforeEach
            void setUp() {
                final CartItem cartItem1 = new CartItem(member, product1, quantity1);
                final CartItem cartItem2 = new CartItem(member, product2, quantity2);
                cartItemRepository.save(cartItem1);
                cartItemRepository.save(cartItem2);

                final CartItem findCartItem1 = cartItemRepository.findOne(cartItem1.getId());
                final CartItem findCartItem2 = cartItemRepository.findOne(cartItem2.getId());
                final OrderRequest orderRequest = new OrderRequest(List.of(findCartItem1.getId(), findCartItem2.getId()), usedPoint);
                final Long orderId = orderService.order(member, orderRequest);
                order = em.find(Order.class, orderId);
            }

            @DisplayName("배송비 3천원 추가한다.")
            @Test
            void it_returns_discounted_delivery_fee() {
                assertAll(
                        () -> assertThat(order.getTotalPrice()).isEqualTo(new ProductPrice(30_000)),
                        () -> assertThat(member.getPoint()).isEqualTo(new MemberPoint(12_000)),
                        () -> assertThat(order.getDeliveryFee()).isEqualTo(new DeliveryFee(3000)),
                        () -> assertThat(order.getUsedPoint()).isEqualTo(new UsedPoint(1000))
                );
            }
        }

        @Nested
        @DisplayName("주문이 완료되면 ")
        class ContextWithOrderProductTest1 {
            private Order order;
            List<OrderProduct> orderProducts;

            @BeforeEach
            void setUp() {
                order = orderRepository.findOne(1L);
                orderProducts = orderProductRepository.findAllByMemberId(member.getId());
            }

            @DisplayName("주문 내역을 볼 수 있다.")
            @Test
            void it_returns_orderProducts() {

                assertAll(
                        () -> assertThat(orderProducts).hasSize(2),

                        () -> assertThat(orderProducts.get(0).getOrder()).isSameAs(order),
                        () -> assertThat(orderProducts.get(0).getProductId()).isEqualTo(product1.getId()),
                        () -> assertThat(orderProducts.get(0).getProductName()).isEqualTo(new ProductName("치킨")),
                        () -> assertThat(orderProducts.get(0).getProductPrice()).isEqualTo(new ProductPrice(10000)),
                        () -> assertThat(orderProducts.get(0).getProductImageUrlValue()).startsWith("https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec"),
                        () -> assertThat(orderProducts.get(0).getQuantity()).isEqualTo(new Quantity(2)),

                        () -> assertThat(orderProducts.get(1).getOrder()).isEqualTo(order),
                        () -> assertThat(orderProducts.get(1).getProductId()).isEqualTo(product2.getId()),
                        () -> assertThat(orderProducts.get(1).getProductName()).isEqualTo(new ProductName("샐러드")),
                        () -> assertThat(orderProducts.get(1).getProductPrice()).isEqualTo(new ProductPrice(20000)),
                        () -> assertThat(orderProducts.get(1).getProductImageUrlValue()).startsWith("https://images.unsplash.com/photo-1512621776951-a57141f2eefd"),
                        () -> assertThat(orderProducts.get(1).getQuantity()).isEqualTo(new Quantity(4))
                );
            }

            @DisplayName("장바구니에 있었던 상품은 제거된다.")
            @Test
            void cart_items_are_deleted() {
                // given
                final CartItem cartItem1 = new CartItem(member, product1, new Quantity(1));
                final CartItem cartItem2 = new CartItem(member, product2, new Quantity(1));
                cartItemRepository.save(cartItem1);
                cartItemRepository.save(cartItem2);

                final OrderRequest orderRequest = new OrderRequest(List.of(cartItem1.getId(), cartItem2.getId()), 0);
                final Long orderId = orderService.order(member, orderRequest);

                final List<CartItem> cartItems = cartItemRepository.findAllByMember(member);

                // when, then
                assertThat(cartItems).doesNotContain(
                        new CartItem(cartItem1.getId(), null, null, new Quantity(0)),
                        new CartItem(cartItem2.getId(), null, null, new Quantity(0))
                );
            }
        }

        @Nested
        @DisplayName("상품의 총 가격이 ")
        class ContextWithOrderProductTest2 {

            @DisplayName("5만원 미만이라면 배송비를 포함한 가격까지 포인트를 사용할 수 있다.")
            @Test
            void pointTest1() {
                // given
                final OrderRequest orderRequest = new OrderRequest(List.of(6L), 3500);
                final Long orderId = orderService.order(member, orderRequest);

                // when
                final Order order = orderRepository.findOne(orderId);

                // then
                assertAll(
                        () -> assertThat(order.getDeliveryFee()).isEqualTo(new DeliveryFee(3000)),
                        () -> assertThat(order.getUsedPoint()).isEqualTo(new UsedPoint(3500)),
                        () -> assertThat(order.getSavedPoint()).isEqualTo(new SavedPoint(50))
                );
            }

            @DisplayName("총 가격과 배송비를 포함한 가격을 초과하는 포인트는 사용할 수 없다.")
            @Test
            void pointTest2() {
                // given
                final OrderRequest orderRequest = new OrderRequest(List.of(6L), 3501);

                // when, then
                assertThatThrownBy(() -> orderService.order(member, orderRequest))
                        .isInstanceOf(InvalidPointUseException.class);
            }
        }

        @DisplayName("주문 id의 리스트로 해당 주문들을 삭제할 수 있다.")
        @Test
        void deleteByIds() {
            // given
            final OrderRequest orderRequest1 = new OrderRequest(List.of(1L), 0);
            final OrderRequest orderRequest2 = new OrderRequest(List.of(2L), 0);
            final OrderRequest orderRequest3 = new OrderRequest(List.of(3L), 0);
            final Long orderId1 = orderService.order(member, orderRequest1);
            final Long orderId2 = orderService.order(member, orderRequest2);
            final Long orderId3 = orderService.order(member, orderRequest3);

            // when
            orderService.deleteByIds(member, List.of(orderId1, orderId2));
            final List<Order> orders = orderService.getAllOrderDetails(member);
            final List<Long> orderIds = orders.stream()
                    .map(Order::getId)
                    .collect(Collectors.toList());

            // then
            assertAll(
                    () -> assertThat(orderIds).doesNotContain(orderId1, orderId2)
            );
        }

        @DisplayName("사용자의 모든 주문을 삭제한다")
        @Test
        void deleteAll() {
            // given
            final OrderRequest orderRequest1 = new OrderRequest(List.of(1L), 0);
            final OrderRequest orderRequest2 = new OrderRequest(List.of(2L), 0);
            final OrderRequest orderRequest3 = new OrderRequest(List.of(3L), 0);
            final Long orderId1 = orderService.order(member, orderRequest1);
            final Long orderId2 = orderService.order(member, orderRequest2);
            final Long orderId3 = orderService.order(member, orderRequest3);

            // when
            orderService.deleteAll(member);
            final List<Order> orders = orderService.getAllOrderDetails(member);

            // then
            assertThat(orders).hasSize(0);
        }
    }
}
