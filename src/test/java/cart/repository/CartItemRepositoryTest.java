package cart.repository;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.notfound.CartItemNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;


    @DisplayName("장바구니 상품을 추가하고 조회할 수 있다.")
    @Test
    void saveAndFind() {
        // given
        final Member member = memberRepository.findOne(2L);
        final Product product = productRepository.findOne(4L);

        final CartItem cartItem = new CartItem(member, product, new Quantity(3));
        cartItemRepository.save(cartItem);

        // when
        final CartItem findCartItem = cartItemRepository.findOne(cartItem.getId());

        // then
        assertAll(
                () -> assertThat(cartItem).isSameAs(findCartItem),
                () -> assertThat(findCartItem.getMember()).isEqualTo(member),
                () -> assertThat(findCartItem.getProduct()).isEqualTo(product),
                () -> assertThat(findCartItem.getQuantity()).isEqualTo(new Quantity(3))
        );
    }

    @DisplayName("특정 Member가 가진 모든 장바구니 상품을 조회할 수 있다.")
    @Test
    void findAllByMemberId() {
        // given
        final Member member = memberRepository.findOne(1L);
        final List<CartItem> cartItems = cartItemRepository.findAllByMember(member);

        // when, then
        assertThat(cartItems).hasSize(6);
    }

    @DisplayName("특정 장바구니 상품 id들로 조회할 수 있다.")
    @Test
    void findAllByIds() {
        // given
        final List<CartItem> cartItems = cartItemRepository.findAllByIds(List.of(1L, 2L, 3L));

        // when, then
        assertThat(cartItems).hasSize(3);
    }

    @DisplayName("장바구니 상품의 수량을 수정할 수 있다.")
    @Test
    void updateQuantity() {
        // given
        final CartItem cartItem = cartItemRepository.findOne(1L);

        // when
        cartItem.updateQuantity(new Quantity(10));

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(new Quantity(10));
    }

    @DisplayName("장바구니 상품의 수량을 하나 증가시킬 수 있다.")
    @Test
    void addOneQuantity() {
        // given
        final CartItem cartItem = cartItemRepository.findOne(1L);
        final Quantity quantity = cartItem.getQuantity();

        // when
        cartItem.updateQuantity(cartItem.getAddedQuantity());

        // then
        assertThat(cartItem.getQuantity().getQuantity()).isEqualTo(quantity.getQuantity() + 1);
    }

    @DisplayName("장바구니 상품을 삭제할 수 있다.")
    @Test
    void delete() {
        // given
        final CartItem cartItem = cartItemRepository.findOne(1L);
        cartItemRepository.delete(cartItem);

        // when, then
        assertThatThrownBy(() -> cartItemRepository.findOne(1L))
                .isInstanceOf(CartItemNotFoundException.class);
    }

    @DisplayName("특정 장바구니 상품들을 삭제할 수 있다.")
    @Test
    void deleteAll() {
        // given
        final CartItem cartItem1 = cartItemRepository.findOne(1L);
        final CartItem cartItem2 = cartItemRepository.findOne(2L);
        final CartItem cartItem3 = cartItemRepository.findOne(3L);
        assertThat(cartItem1).isNotNull();
        assertThat(cartItem2).isNotNull();
        assertThat(cartItem3).isNotNull();

        // when
        cartItemRepository.deleteAll(
                List.of(cartItem1, cartItem2, cartItem3)
        );

        // then
        assertAll(
                () -> assertThatThrownBy(() -> cartItemRepository.findOne(1L))
                        .isInstanceOf(CartItemNotFoundException.class),
                () -> assertThatThrownBy(() -> cartItemRepository.findOne(2L))
                        .isInstanceOf(CartItemNotFoundException.class),
                () -> assertThatThrownBy(() -> cartItemRepository.findOne(3L))
                        .isInstanceOf(CartItemNotFoundException.class)
        );
    }

    @DisplayName("특정 장바구니 id들의 상품들을 삭제할 수 있다.")
    @Test
    void deleteByIds() {
        // given
        final CartItem cartItem1 = cartItemRepository.findOne(1L);
        final CartItem cartItem2 = cartItemRepository.findOne(2L);
        final CartItem cartItem3 = cartItemRepository.findOne(3L);
        assertAll(
                () -> assertThat(cartItem1).isNotNull(),
                () -> assertThat(cartItem2).isNotNull(),
                () -> assertThat(cartItem3).isNotNull()
        );

        // when
        cartItemRepository.deleteByIds(
                List.of(cartItem1.getId(), cartItem2.getId(), cartItem3.getId())
        );

        // then
        assertAll(
                () -> assertThatThrownBy(() -> cartItemRepository.findOne(1L))
                        .isInstanceOf(CartItemNotFoundException.class),
                () -> assertThatThrownBy(() -> cartItemRepository.findOne(2L))
                        .isInstanceOf(CartItemNotFoundException.class),
                () -> assertThatThrownBy(() -> cartItemRepository.findOne(3L))
                        .isInstanceOf(CartItemNotFoundException.class)
        );
    }
}
