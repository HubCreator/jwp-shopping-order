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

    private final MemberDataJpaRepository memberRepository;
    private final CartItemDataJpaRepository cartItemRepository;
    private final OrderDataJpaRepository orderRepository;
    private final ProductDataJpaRepository productRepository;

    @Transactional
    public Long order(final Auth auth, final OrderRequest request) {
        final Member member = getMember(auth);
        final CartItems cartItems = new CartItems(cartItemRepository.findAllById(request.getCartItemIds()));
        cartItems.checkOwner(member);
        updateMemberPoint(request, member, cartItems);
        final Order order = getOrder(request, member, cartItems);
        cartItemRepository.deleteAll(cartItems.getCartItems());
        return order.getId();
    }

    private void validateInvalidPointUse(final OrderRequest request, final CartItems cartItems, final ProductPrice totalPrice) {
        // TODO 너무 객체지향적이지 않음
        if (totalPrice.getPrice() + cartItems.getDeliveryFee().getDeliveryFee() < request.getPoint()) {
            throw new InvalidPointUseException(totalPrice, request.getPoint());
        }
    }

    private void updateMemberPoint(final OrderRequest request, final Member member, final CartItems cartItems) {
        final ProductPrice totalPrice = cartItems.getTotalPrice();
        validateInvalidPointUse(request, cartItems, totalPrice);
        member.updatePoint(new MemberPoint(request.getPoint()), totalPrice);
    }

    private Order getOrder(final OrderRequest request, final Member member, final CartItems cartItems) {
        final Order order = new Order(member, new UsedPoint(request.getPoint()), cartItems.getSavedPoint(), cartItems.getDeliveryFee());
        final List<Product> products = productRepository.findAllById(cartItems.getProductIds());
        final List<OrderProduct> orderProducts = cartItems.toOrderProducts(order, products);
        order.updateOrderProduct(orderProducts);
        orderRepository.save(order);
        return order;
    }

    public Order getOrderDetail(final Auth auth, final Long orderId) {
        final Member member = getMember(auth);
        final Order order = orderRepository.findByIdWithOrderItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.checkOwner(member);
        return order;
    }

    public List<Order> getAllOrderDetails(final Auth auth) {
        final Member member = getMember(auth);
        final List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        orders.forEach(order -> order.checkOwner(member));
        return orders;
    }

    @Transactional
    public void deleteByIds(final Auth auth, final List<Long> orderIds) {
        final Member member = getMember(auth);
        final List<Order> orders = orderRepository.findAllById(orderIds);
        orders.forEach(order -> order.checkOwner(member));
        orderRepository.deleteAll(orders);
    }

    @Transactional
    public void deleteAll(final Auth auth) {
        final Member member = getMember(auth);
        final List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        orders.forEach(order -> order.checkOwner(member));
        orderRepository.deleteAll(orders);
    }

    private Member getMember(final Auth auth) {
        return memberRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(auth.getEmail()));
    }
}
