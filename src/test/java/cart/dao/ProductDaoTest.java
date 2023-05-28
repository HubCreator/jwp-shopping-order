package cart.dao;

import cart.domain.product.Product;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Import(ProductDao.class)
public class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    final Product product = new Product("초콜릿", 500, "초콜릿URL");


    @DisplayName("상품을 추가하고 찾아온다.")
    @Test
    void createAndGetProduct() {
        // given
        final Long productId = productDao.save(product);

        // when
        final Product findProduct = productDao.getProductById(productId);

        // then
        assertAll(
                () -> assertThat(findProduct.getProductName()).isEqualTo(new ProductName("초콜릿")),
                () -> assertThat(findProduct.getProductPrice()).isEqualTo(new ProductPrice(500)),
                () -> assertThat(findProduct.getProductImageUrl()).isEqualTo(new ProductImageUrl("초콜릿URL"))
        );
    }

    @DisplayName("List<Long> productIds로 Product들을 찾아온다.")
    @Test
    void getProductsByIds() {
        // given
        final Product product1 = new Product("초콜릿1", 501, "초콜릿URL1");
        final Product product2 = new Product("초콜릿2", 502, "초콜릿URL2");
        final Product product3 = new Product("초콜릿3", 503, "초콜릿URL3");
        final Long productId1 = productDao.save(product1);
        final Long productId2 = productDao.save(product2);
        final Long productId3 = productDao.save(product3);

        // when
        final List<Product> products = productDao.getProductsByIds(List.of(productId1, productId2, productId3));

        // then
        assertAll(
                () -> assertThat(products).hasSize(3),
                () -> assertThat(products.get(0).getId()).isEqualTo(productId1),
                () -> assertThat(products.get(1).getId()).isEqualTo(productId2),
                () -> assertThat(products.get(2).getId()).isEqualTo(productId3)
        );
    }

    @DisplayName("상품을 수정할 수 있다.")
    @Test
    void updateProduct() {
        // given
        final Long productId = productDao.save(product);
        final Product findProduct = productDao.getProductById(productId);
        final Product updateProduct = new Product(findProduct.getId(), "초콜릿아님", findProduct.getPriceValue(), findProduct.getImageUrlValue());

        // when
        productDao.updateProduct(productId, updateProduct);
        final Product updatedProduct = productDao.getProductById(productId);

        // then
        assertThat(updatedProduct.getProductName()).isEqualTo(new ProductName("초콜릿아님"));
    }

    @DisplayName("상품을 삭제할 수 있다.")
    @Test
    void deleteProduct() {
        // given
        final Long productId = productDao.save(product);

        // when
        productDao.deleteProduct(productId);

        // then
        assertThatThrownBy(() -> productDao.getProductById(productId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}