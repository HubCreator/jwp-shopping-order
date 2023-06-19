package cart.ui;

import cart.application.CartItemService;
import cart.application.dto.cartitem.CartItemIdsRequest;
import cart.application.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.application.dto.cartitem.CartItemRequest;
import cart.domain.member.Member;
import cart.ui.dto.cartitem.CartItemResponse;
import cart.ui.dto.cartitem.CartItemsPriceResponse;
import cart.ui.dto.cartitem.TotalPriceAndDeliveryFeeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(Member member,
                                             @Valid @RequestBody CartItemRequest request) {
        final Long cartItemId = cartItemService.addCartItem(member, request);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(Member member) {
        final List<CartItemResponse> cartItemResponses = cartItemService.getCartItemsByMember(member)
                .stream()
                .map(CartItemResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cartItemResponses);
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(Member member,
                                                       @PathVariable Long cartItemId,
                                                       @Valid @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, cartItemId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(Member member,
                                               @PathVariable Long cartItemId) {
        cartItemService.removeCartItem(member, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeCartItems(Member member,
                                                @Valid @RequestBody CartItemIdsRequest request) {
        cartItemService.removeCartItems(member, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/price")
    public ResponseEntity<CartItemsPriceResponse> getCartItemsPrice(Member member,
                                                                    @RequestParam List<Long> item) {
        final TotalPriceAndDeliveryFeeDto resultDto = cartItemService.getPaymentInfo(member, item);
        return ResponseEntity.ok(new CartItemsPriceResponse(resultDto));
    }
}
