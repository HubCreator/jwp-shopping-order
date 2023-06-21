package cart.application;

import cart.application.dto.cartitem.CartItemRequest;
import cart.domain.auth.Auth;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.member.MemberEmail;
import cart.repository.datajpa.CartItemDataJpaRepository;
import cart.repository.datajpa.MemberDataJpaRepository;
import cart.ui.dto.cartitem.TotalPriceAndDeliveryFeeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
public class CartItemServiceTest {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private MemberDataJpaRepository memberRepository;
    @Autowired
    private CartItemDataJpaRepository cartItemRepository;

    private Auth auth;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.findByEmail(new MemberEmail("a@a.com"))
                .orElseThrow();
        auth = new Auth(member.getEmail(), member.getPassword());
    }

    @DisplayName("장바구니에 상품을 추가할 때, 이미 해당 상품이 존재하면 수량만 하나 추가한다.")
    @Test
    void addCartItem() {
        // given
        final CartItemRequest cartItemRequest = new CartItemRequest(1L);
        final Member findMember = memberRepository.findByEmail(auth.getEmail()).orElseThrow();

        // when
        cartItemService.addCartItem(auth, cartItemRequest);
        final CartItem cartItem = cartItemRepository.findById(1L).orElseThrow();
        final List<CartItem> cartItems = cartItemRepository.findAllByMemberId(findMember.getId());

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(new Quantity(3));
        assertThat(cartItems).hasSize(6);
    }

    @DisplayName("장바구니 상품에 대한 총 가격과 배송비를 구한다.")
    @Test
    void getPaymentInfo1() {
        // given
        final List<Long> cartItemIds = List.of(1L, 2L, 3L);

        // when
        final TotalPriceAndDeliveryFeeDto resultDto = cartItemService.getPaymentInfo(auth, cartItemIds);

        // then
        assertAll(
                () -> assertThat(resultDto.getTotalPrice()).isEqualTo(120_000),
                () -> assertThat(resultDto.getDeliveryFee()).isEqualTo(0)
        );
    }

    @DisplayName("장바구니 상품에 대한 총 가격과 배송비를 구한다.")
    @Test
    void getPaymentInfo2() {
        // given
        final List<Long> cartItemIds = List.of(1L);

        // when
        final TotalPriceAndDeliveryFeeDto resultDto = cartItemService.getPaymentInfo(auth, cartItemIds);

        // then
        assertAll(
                () -> assertThat(resultDto.getTotalPrice()).isEqualTo(20_000),
                () -> assertThat(resultDto.getDeliveryFee()).isEqualTo(3000)
        );
    }
}
