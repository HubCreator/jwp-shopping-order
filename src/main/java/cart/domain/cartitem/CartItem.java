package cart.domain.cartitem;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.authorization.CartItemAccessForbiddenException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Quantity quantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public CartItem(final Member member, final Product product) {
        this(member, product, Quantity.create());
    }

    public CartItem(final Member member, final Product product, final Quantity quantity) {
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }

    public CartItem(final long id, final Member member, final Product product, final Quantity quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member, member)) {
            throw new CartItemAccessForbiddenException(member.getEmail());
        }
    }

    public CartItem updateQuantity(final Quantity quantity) {
        return new CartItem(id, member, product, quantity);
    }

    public CartItem addQuantity() {
        return new CartItem(id, member, product, quantity.add());
    }

    public void update(final CartItem cartItem) {
        this.id = cartItem.id;
        this.member = cartItem.member;
        this.product = cartItem.product;
        this.quantity = cartItem.quantity;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Long getProductId() {
        return product.getId();
    }

    public int getProductPrice() {
        return product.getPriceValue();
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

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", product=" + product +
                ", member=" + member +
                '}';
    }
}
