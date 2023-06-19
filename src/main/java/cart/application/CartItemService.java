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
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
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

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public Long addCartItem(final Auth auth, final CartItemRequest request) {
        final Member member = memberRepository.findByEmail(auth.getEmail());
        final Product product = productRepository.findOne(request.getProductId());
        final CartItems cartItems = new CartItems(cartItemRepository.findAllByMember(member));
        final Optional<CartItem> cartItemOptional = cartItems.findProduct(product);
        if (cartItemOptional.isEmpty()) {
            final CartItem cartItem = new CartItem(member, product);
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
        final CartItem cartItem = cartItemOptional.get();
        cartItem.checkOwner(member);
        final Quantity addedQuantity = cartItem.getAddedQuantity();
        cartItemRepository.updateQuantity(cartItem, addedQuantity);

        return cartItem.getId();
    }

    public List<CartItem> getCartItemsByMember(final Auth auth) {
        final Member member = memberRepository.findByEmail(auth.getEmail());
        return cartItemRepository.findAllByMember(member);
    }

    @Transactional
    public void updateQuantity(final Auth auth, final Long cartItemId, final CartItemQuantityUpdateRequest request) {
        final Member member = memberRepository.findByEmail(auth.getEmail());
        final CartItem cartItem = cartItemRepository.findOne(cartItemId);
        cartItem.checkOwner(member);
        if (request.getQuantity() == 0) {
            cartItemRepository.delete(cartItem);
            return;
        }
        cartItem.updateQuantity(new Quantity(request.getQuantity()));
    }

    @Transactional
    public void removeCartItem(final Auth auth, final Long cartItemId) {
        final Member member = memberRepository.findByEmail(auth.getEmail());
        final CartItem cartItem = cartItemRepository.findOne(cartItemId);
        cartItem.checkOwner(member);
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void removeCartItems(final Auth auth, final CartItemIdsRequest request) {
        final Member member = memberRepository.findByEmail(auth.getEmail());
        final CartItems cartItems = new CartItems(cartItemRepository.findAllByIds(request.getCartItemIds()));
        cartItems.checkOwner(member);
        cartItemRepository.deleteAll(cartItems.getCartItems());
    }

    public TotalPriceAndDeliveryFeeDto getPaymentInfo(final Auth auth, final List<Long> cartItemIds) {
        final Member member = memberRepository.findByEmail(auth.getEmail());
        final CartItems cartItems = new CartItems(cartItemRepository.findAllByIds(cartItemIds));
        cartItems.checkOwner(member);
        return new TotalPriceAndDeliveryFeeDto(cartItems.getTotalPrice(), cartItems.getDeliveryFee());
    }
}
