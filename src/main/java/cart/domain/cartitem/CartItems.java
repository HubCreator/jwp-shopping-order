package cart.domain.cartitem;

import cart.domain.member.Member;
import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.order.SavedPoint;
import cart.domain.product.Product;
import cart.domain.product.ProductPrice;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class CartItems {

    private static final DeliveryFee DELIVERY_FEE = new DeliveryFee(3_000);
    private static final ProductPrice SALE_THRESHOLD = new ProductPrice(50_000);
    private static final double SALE_RATE = 0.1;

    private final List<CartItem> cartItems;
    private final ProductPrice totalPrice;
    private final DeliveryFee deliveryFee;
    private final SavedPoint savedPoint;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.totalPrice = calculateTotalPrice(cartItems);
        this.deliveryFee = calculateDeliveryFee();
        this.savedPoint = calculateSavedPoint();
    }

    private SavedPoint calculateSavedPoint() {
        final ProductPrice appliedSalePrice = totalPrice.applySale(SALE_RATE);
        return new SavedPoint(appliedSalePrice.getPrice());
    }

    private ProductPrice calculateTotalPrice(final List<CartItem> cartItems) {
        final int totalPrice = cartItems.stream()
                .mapToInt(cartitem -> cartitem.getProductPrice() * cartitem.getQuantityValue())
                .sum();
        return new ProductPrice(totalPrice);
    }

    private DeliveryFee calculateDeliveryFee() {
        if (totalPrice.isOverOrEqualThan(SALE_THRESHOLD)) {
            return DeliveryFee.none();
        }
        return DELIVERY_FEE;
    }

    public void checkOwner(final Member member) {
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
    }

    public List<OrderProduct> toOrderProducts(final Order order, final List<Product> products) {
        return IntStream.range(0, products.size())
                .mapToObj(index -> new OrderProduct(
                        order,
                        products.get(index),
                        products.get(index).getProductName(),
                        products.get(index).getProductPrice(),
                        products.get(index).getProductImageUrl(),
                        cartItems.get(index).getQuantity())
                ).collect(Collectors.toList());
    }

    public List<Long> getProductIds() {
        return cartItems.stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());
    }

    public List<Long> getCartItemIds() {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
    }

    public Optional<CartItem> findProduct(final Product product) {
        return cartItems.stream()
                .filter(m -> m.getProduct().equals(product))
                .findFirst();
    }
}
