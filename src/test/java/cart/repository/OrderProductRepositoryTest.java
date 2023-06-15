package cart.repository;

import cart.domain.cartitem.CartItem;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.ProductName;
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
class OrderProductRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

    @DisplayName("주문에 해당하는 주문 상품을 조회할 수 있다.")
    @Test
    void findAllByOrderId() {
        // given
        final Order order = em.find(Order.class, 1L);
        final CartItem cartItem = em.find(CartItem.class, 1L);
        final List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderId(order.getId());

        // then
        assertAll(
                () -> assertThat(orderProducts).hasSize(2),
                () -> assertThat(orderProducts.get(0).getProductName()).isEqualTo(new ProductName("치킨")),
                () -> assertThat(orderProducts.get(1).getProductName()).isEqualTo(new ProductName("샐러드"))
        );

    }
}
