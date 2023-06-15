package cart.config;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.member.MemberEmail;
import cart.domain.member.MemberPassword;
import cart.domain.member.MemberPoint;
import cart.domain.product.Product;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("test")
@AllArgsConstructor
public class InitData implements CommandLineRunner {

    private InitService initService;

    @Override
    public void run(final String... args) {
        initService.init();
    }

    @Component
    static class InitService {

        @Autowired
        private ProductRepository productRepository;
        @Autowired
        private MemberRepository memberRepository;
        @Autowired
        private CartItemRepository cartItemRepository;

        @Transactional
        public void init() {
            final Product product1 = new Product(new ProductName("치킨"), new ProductPrice(10_000), new ProductImageUrl("https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"));
            final Product product2 = new Product(new ProductName("샐러드"), new ProductPrice(20_000), new ProductImageUrl("https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"));
            final Product product3 = new Product(new ProductName("피자"), new ProductPrice(13_000), new ProductImageUrl("https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"));
            final Product product4 = new Product(new ProductName("초밥"), new ProductPrice(18_000), new ProductImageUrl("https://search.pstatic.net/common/?https://cdn-icons-png.flaticon.com/128/5391/5391494.png"));
            final Product product5 = new Product(new ProductName("사탕"), new ProductPrice(500), new ProductImageUrl("사탕URL"));
            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            productRepository.save(product5);

            final Member member1 = new Member(new MemberEmail("a@a.com"), new MemberPassword("1234"), new MemberPoint(10_000));
            final Member member2 = new Member(new MemberEmail("b@b.com"), new MemberPassword("1234"), new MemberPoint(10_000));
            memberRepository.save(member1);
            memberRepository.save(member2);

            final CartItem cartItem1 = new CartItem(member1, product1, new Quantity(2));
            final CartItem cartItem2 = new CartItem(member1, product2, new Quantity(4));
            final CartItem cartItem3 = new CartItem(member1, product2, new Quantity(1));
            final CartItem cartItem4 = new CartItem(member1, product3, new Quantity(2));
            final CartItem cartItem5 = new CartItem(member1, product4, new Quantity(2));
            final CartItem cartItem6 = new CartItem(member1, product5, new Quantity(1));
            cartItemRepository.save(cartItem1);
            cartItemRepository.save(cartItem2);
            cartItemRepository.save(cartItem3);
            cartItemRepository.save(cartItem4);
            cartItemRepository.save(cartItem5);
            cartItemRepository.save(cartItem6);

            final CartItem cartItem7 = new CartItem(member2, product2, new Quantity(5));
            cartItemRepository.save(cartItem7);
        }
    }
}
