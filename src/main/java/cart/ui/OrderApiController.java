package cart.ui;

import cart.application.OrderService;
import cart.application.dto.order.OrderRequest;
import cart.domain.auth.Auth;
import cart.domain.order.Order;
import cart.ui.dto.order.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> order(final Auth auth,
                                      @Valid @RequestBody OrderRequest request) {
        final Long orderId = orderService.order(auth, request);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> orderDetail(final Auth auth,
                                                           @PathVariable Long orderId) {
        final Order order = orderService.getOrderDetail(auth, orderId);
        return ResponseEntity.ok(new OrderDetailResponse(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailResponse>> orderDetails(final Auth auth) {
        final List<Order> orders = orderService.getAllOrderDetails(auth);
        final List<OrderDetailResponse> response = orders.stream()
                .map(OrderDetailResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(final Auth auth,
                                            @RequestParam List<Long> orderId) {
        orderService.deleteByIds(auth, orderId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll(final Auth auth) {
        orderService.deleteAll(auth);
        return ResponseEntity.noContent().build();
    }
}
