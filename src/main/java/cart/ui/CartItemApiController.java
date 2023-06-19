package cart.ui;

import cart.application.CartItemService;
import cart.application.dto.cartitem.CartItemIdsRequest;
import cart.application.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.application.dto.cartitem.CartItemRequest;
import cart.domain.auth.Auth;
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
    public ResponseEntity<Void> addCartItems(final Auth auth,
                                             @Valid @RequestBody CartItemRequest request) {
        final Long cartItemId = cartItemService.addCartItem(auth, request);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(final Auth auth) {
        final List<CartItemResponse> cartItemResponses = cartItemService.getCartItemsByMember(auth)
                .stream()
                .map(CartItemResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cartItemResponses);
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(final Auth auth,
                                                       @PathVariable Long cartItemId,
                                                       @Valid @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(auth, cartItemId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(final Auth auth,
                                               @PathVariable Long cartItemId) {
        cartItemService.removeCartItem(auth, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeCartItems(final Auth auth,
                                                @Valid @RequestBody CartItemIdsRequest request) {
        cartItemService.removeCartItems(auth, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/price")
    public ResponseEntity<CartItemsPriceResponse> getCartItemsPrice(final Auth auth,
                                                                    @RequestParam List<Long> item) {
        final TotalPriceAndDeliveryFeeDto resultDto = cartItemService.getPaymentInfo(auth, item);
        return ResponseEntity.ok(new CartItemsPriceResponse(resultDto));
    }
}
