package cart.application;

import cart.application.dto.cartitem.CartItemIdsRequest;
import cart.application.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.application.dto.cartitem.CartItemRequest;
import cart.domain.auth.Auth;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.notfound.CartItemNotFoundException;
import cart.exception.notfound.MemberNotFoundException;
import cart.exception.notfound.ProductNotFoundException;
import cart.repository.datajpa.CartItemDataJpaRepository;
import cart.repository.datajpa.MemberDataJpaRepository;
import cart.repository.datajpa.ProductDataJpaRepository;
import cart.ui.dto.cartitem.TotalPriceAndDeliveryFeeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartItemService {

    private final MemberDataJpaRepository memberDataJpaRepository;
    private final ProductDataJpaRepository productDataJpaRepository;
    private final CartItemDataJpaRepository cartItemDataJpaRepository;

    @Transactional
    public Long addCartItem(final Auth auth, final CartItemRequest request) {
        final Member member = memberDataJpaRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
        final Product product = productDataJpaRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        final CartItems cartItems = new CartItems(cartItemDataJpaRepository.findAllByMemberId(member.getId()));
        final Optional<CartItem> cartItemOptional = cartItems.findProduct(product);
        if (cartItemOptional.isEmpty()) {
            final CartItem cartItem = new CartItem(member, product);
            cartItemDataJpaRepository.save(cartItem);
            return cartItem.getId();
        }
        final CartItem cartItem = cartItemOptional.get();
        cartItem.checkOwner(member);
        cartItem.addQuantity();

        return cartItem.getId();
    }

    public List<CartItem> getCartItemsByMember(final Auth auth) {
        final Member member = memberDataJpaRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
        return cartItemDataJpaRepository.findAllByMemberId(member.getId());
    }

    @Transactional
    public void updateQuantity(final Auth auth, final Long cartItemId, final CartItemQuantityUpdateRequest request) {
        final Member member = memberDataJpaRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
        final CartItem cartItem = cartItemDataJpaRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        cartItem.checkOwner(member);
        if (request.getQuantity() == 0) {
            cartItemDataJpaRepository.delete(cartItem);
            return;
        }
        cartItem.updateQuantity(new Quantity(request.getQuantity()));
    }

    @Transactional
    public void removeCartItem(final Auth auth, final Long cartItemId) {
        final Member member = memberDataJpaRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
        final CartItem cartItem = cartItemDataJpaRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        cartItem.checkOwner(member);
        cartItemDataJpaRepository.delete(cartItem);
    }

    @Transactional
    public void removeCartItems(final Auth auth, final CartItemIdsRequest request) {
        final Member member = memberDataJpaRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
        final CartItems cartItems = new CartItems(cartItemDataJpaRepository.findAllById(request.getCartItemIds()));
        cartItems.checkOwner(member);
        cartItemDataJpaRepository.deleteAll(cartItems.getCartItems());
    }

    public TotalPriceAndDeliveryFeeDto getPaymentInfo(final Auth auth, final List<Long> cartItemIds) {
        final Member member = memberDataJpaRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
        final CartItems cartItems = new CartItems(cartItemDataJpaRepository.findAllById(cartItemIds));
        cartItems.checkOwner(member);
        return new TotalPriceAndDeliveryFeeDto(cartItems.getTotalPrice(), cartItems.getDeliveryFee());
    }
}
