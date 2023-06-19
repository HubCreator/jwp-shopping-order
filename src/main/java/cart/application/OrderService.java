package cart.application;

import cart.application.dto.order.OrderRequest;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.order.Order;
import cart.domain.order.UsedPoint;
import cart.domain.product.ProductPrice;
import cart.exception.business.order.InvalidPointUseException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderProductRepository;
import cart.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public Long order(final Member member, final OrderRequest request) {
        final Member findMember = memberRepository.findByEmail(member.getEmail());
        final CartItems cartItems = new CartItems(cartItemRepository.findAllByIds(request.getCartItemIds()));
        cartItems.checkOwner(findMember);
        final ProductPrice totalPrice = cartItems.getTotalPrice();
        validateInvalidPointUse(request, cartItems, totalPrice);
        findMember.updatePoint(new MemberPoint(request.getPoint()), totalPrice);
        final Long orderId = orderRepository.save(cartItems, findMember, new UsedPoint(request.getPoint()));
        cartItemRepository.deleteByIds(cartItems.getCartItemIds());
        return orderId;
    }

    private void validateInvalidPointUse(final OrderRequest request, final CartItems cartItems, final ProductPrice totalPrice) {
        // TODO 너무 객체지향적이지 않음
        if (totalPrice.getPrice() + cartItems.getDeliveryFee().getDeliveryFee() < request.getPoint()) {
            throw new InvalidPointUseException(totalPrice, request.getPoint());
        }
    }

    public Order getOrderDetail(final Member member, final Long orderId) {
        final Order order = orderRepository.findOneWithOrderItems(orderId);
        order.checkOwner(member);
        return order;
    }

    public List<Order> getAllOrderDetails(final Member member) {
        final List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        orders.forEach(order -> order.checkOwner(member));
        return orders;
    }

    @Transactional
    public void deleteByIds(final Member member, final List<Long> orderIds) {
        final List<Order> orders = orderRepository.findAllByOrderIds(orderIds);
        orders.forEach(order -> order.checkOwner(member));
        orderRepository.deleteAll(orders);
    }

    @Transactional
    public void deleteAll(final Member member) {
        final List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        orders.forEach(order -> order.checkOwner(member));
        orderRepository.deleteAll(orders);
    }
}
