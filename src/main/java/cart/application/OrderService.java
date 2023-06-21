package cart.application;

import cart.application.dto.order.OrderRequest;
import cart.domain.auth.Auth;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.order.UsedPoint;
import cart.domain.product.Product;
import cart.domain.product.ProductPrice;
import cart.exception.business.order.InvalidPointUseException;
import cart.exception.notfound.MemberNotFoundException;
import cart.exception.notfound.OrderNotFoundException;
import cart.repository.datajpa.CartItemDataJpaRepository;
import cart.repository.datajpa.MemberDataJpaRepository;
import cart.repository.datajpa.OrderDataJpaRepository;
import cart.repository.datajpa.ProductDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberDataJpaRepository memberDataJpaRepository;
    private final CartItemDataJpaRepository cartItemDataJpaRepository;
    private final OrderDataJpaRepository orderDataJpaRepository;
    private final ProductDataJpaRepository productDataJpaRepository;

    @Transactional
    public Long order(final Auth auth, final OrderRequest request) {
        final Member member = memberDataJpaRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
        final CartItems cartItems = new CartItems(cartItemDataJpaRepository.findAllById(request.getCartItemIds()));
        cartItems.checkOwner(member);
        final ProductPrice totalPrice = cartItems.getTotalPrice();
        validateInvalidPointUse(request, cartItems, totalPrice);
        member.updatePoint(new MemberPoint(request.getPoint()), totalPrice);
        final Order order = new Order(member, new UsedPoint(request.getPoint()), cartItems.getSavedPoint(), cartItems.getDeliveryFee());
        final List<Product> products = productDataJpaRepository.findAllById(cartItems.getProductIds());
        final List<OrderProduct> orderProducts = cartItems.toOrderProducts(order, products);
        order.updateOrderProduct(orderProducts);
        orderDataJpaRepository.save(order);
        cartItemDataJpaRepository.deleteAll(cartItems.getCartItems());
        return order.getId();
    }

    private void validateInvalidPointUse(final OrderRequest request, final CartItems cartItems, final ProductPrice totalPrice) {
        // TODO 너무 객체지향적이지 않음
        if (totalPrice.getPrice() + cartItems.getDeliveryFee().getDeliveryFee() < request.getPoint()) {
            throw new InvalidPointUseException(totalPrice, request.getPoint());
        }
    }

    public Order getOrderDetail(final Auth auth, final Long orderId) {
        final Member member = memberDataJpaRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
        final Order order = orderDataJpaRepository.findByIdWithOrderItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.checkOwner(member);
        return order;
    }

    public List<Order> getAllOrderDetails(final Auth auth) {
        final Member member = memberDataJpaRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
        final List<Order> orders = orderDataJpaRepository.findAllByMemberId(member.getId());
        orders.forEach(order -> order.checkOwner(member));
        return orders;
    }

    @Transactional
    public void deleteByIds(final Auth auth, final List<Long> orderIds) {
        final Member member = memberDataJpaRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
        final List<Order> orders = orderDataJpaRepository.findAllByOrderIds(orderIds);
        orders.forEach(order -> order.checkOwner(member));
        orderDataJpaRepository.deleteAll(orders);
    }

    @Transactional
    public void deleteAll(final Auth auth) {
        final Member member = memberDataJpaRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
        final List<Order> orders = orderDataJpaRepository.findAllByMemberId(member.getId());
        orders.forEach(order -> order.checkOwner(member));
        orderDataJpaRepository.deleteAll(orders);
    }
}
