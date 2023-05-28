package cart.domain.cartitem;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {

    private final Long id;
    private Quantity quantity;
    private final Product product;
    private final Member member;

    public CartItem(final Member member, final Product product) {
        this(null, member, product, 1);
    }

    public CartItem(final Member member, final Product product, final int quantity) {
        this(null, member, product, quantity);
    }

    public CartItem(final Long id, final Member member, final Product product, final int quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = new Quantity(quantity);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public Long getProductId(){
        return product.getId();
    }

    public int getProductPrice() {
        return product.getPriceValue();
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public int getQuantityValue() {
        return quantity.getQuantity();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}