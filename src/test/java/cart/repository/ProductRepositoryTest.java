package cart.repository;

import cart.domain.product.Product;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import cart.repository.datajpa.ProductDataJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductDataJpaRepository productDataJpaRepository;

    @DisplayName("상품을 저장하고 조회할 수 있다.")
    @Test
    void saveAndFind() {
        // given
        final Product product = new Product(
                new ProductName("신제품"),
                new ProductPrice(30_000),
                new ProductImageUrl("신제품Url")
        );
        productDataJpaRepository.save(product);

        // when
        assertThat(product.getId()).isNotNull();
        final Product findProduct = productDataJpaRepository.findById(product.getId()).orElseThrow();

        // then
        assertThat(findProduct).isSameAs(product);
    }

    @DisplayName("모든 상품을 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        final List<Product> products = productDataJpaRepository.findAll();

        // when
        assertThat(products).hasSize(5);
    }

    @DisplayName("특정 id에 해당하는 상품을 조회할 수 있다.")
    @Test
    void findAllByIds() {
        // given
        final List<Long> ids = List.of(1L, 2L, 3L);
        final List<Product> products = productDataJpaRepository.findAllById(ids);

        // when, then
        assertAll(
                () -> assertThat(products).hasSize(3),
                () -> assertThat(products.get(0)).isNotNull(),
                () -> assertThat(products.get(1)).isNotNull(),
                () -> assertThat(products.get(2)).isNotNull()
        );
    }

    @DisplayName("기존 상품의 속성을 수정할 수 있다.")
    @Test
    void updateProduct() {
        // given
        final Product product = productDataJpaRepository.findById(1L).orElseThrow();
        final Product newProduct = new Product(
                new ProductName("바뀐상품"),
                new ProductPrice(111),
                new ProductImageUrl("바뀐상품Url")
        );
        product.updateTo(newProduct);
//        em.flush();
//        em.clear();

        // when
        final Product updatedProduct = productDataJpaRepository.findById(1L).orElseThrow();

        // then
        assertAll(
                () -> assertThat(updatedProduct.getId()).isEqualTo(1L),
                () -> assertThat(updatedProduct.getProductName()).isEqualTo(new ProductName("바뀐상품")),
                () -> assertThat(updatedProduct.getProductPrice()).isEqualTo(new ProductPrice(111)),
                () -> assertThat(updatedProduct.getProductImageUrl()).isEqualTo(new ProductImageUrl("바뀐상품Url"))
        );
    }

    @DisplayName("상품을 삭제할 수 있다.")
    @Test
    void delete() {
        // given
        productDataJpaRepository.deleteById(1L);

        // when, then
        assertThat(productDataJpaRepository.findById(1L)).isEmpty();
    }
}
