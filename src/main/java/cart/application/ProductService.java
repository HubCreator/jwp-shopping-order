package cart.application;

import cart.application.dto.product.ProductRequest;
import cart.domain.product.Product;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import cart.exception.notfound.ProductNotFoundException;
import cart.repository.datajpa.ProductDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductDataJpaRepository productRepository;

    @Transactional
    public Long createProduct(final ProductRequest request) {
        final Product product = new Product(
                new ProductName(request.getName()),
                new ProductPrice(request.getPrice()),
                new ProductImageUrl(request.getImageUrl()));
        productRepository.save(product);
        return product.getId();
    }

    public Product getProductById(final Long productId) {
        return getProduct(productId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public void updateProduct(final Long productId, final ProductRequest request) {
        final Product findProduct = getProduct(productId);
        final Product updatedProduct = new Product(
                productId,
                new ProductName(request.getName()),
                new ProductPrice(request.getPrice()),
                new ProductImageUrl(request.getImageUrl()));
        findProduct.updateTo(updatedProduct);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        final Product product = getProduct(productId);
        productRepository.delete(product);
    }

    private Product getProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
